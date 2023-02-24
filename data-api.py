from flask import Flask, request, jsonify

import pandas as pd
import cv2
import base64 
from random import Random
from flask import Flask
from flask_mysqldb import MySQL
global mysql
mysql = MySQL()

app = Flask(__name__)

app.config['MYSQL_HOST'] = "localhost"
app.config['MYSQL_USER'] = "root"
app.config['MYSQL_PASSWORD'] = ""
app.config['MYSQL_DB'] = "somaticcellcounter"
app.config['SECRET_KEY'] = "yuhu"
#mysql.init_app(app) # Initialise with the new app
mysql = MySQL(app) 

#fungsi ngasih warna
def random_color(random):
    icolor = random.randint(0, 0xFFFFFF)
    return [icolor & 0xff, (icolor >> 8) & 0xff, (icolor >> 16) & 0xff]

@app.route('/', methods=["GET", "POST"])
def index():    
    #ketika di ada gambar yang diupload di app maka
    if request.method == 'POST': 
        #ambil data gambar berbentuk base 64
        details = request.form
        #simpan di variabel data
        data = details['Data'] 

        #start
        #imgdata menampung data dan didecode kembali     
        imgdata = base64.b64decode(data)        
        #hasil decode di tulis ulang menjadi gambar kembali tadinya base64 -> jpg
        filename = 'input.jpg'
        with open(filename, 'wb') as f:
            f.write(imgdata)
        #baca gambar yang barusan di proses
        img = cv2.imread('input.jpg', 0)        

        #proses 1
        #gambar di blur supaya yang kecil2 tidak terlalu kelihatan ketika di deteksi
        img = cv2.medianBlur(img,5)        

        #proses 2
        #mencari rata2 dari gambar untuk dibuat masking
        avgPixelIntensity = cv2.mean( img )
        
        #gambar nya di buat hitam putih pada inRange, hitam titik2 bulatan, putih selain bulatan
        mask = cv2.inRange(img, avgPixelIntensity[0]*0.8, 255) 
        #gambarnya dibalik sekarang hitamnya warnanya putih, selainnya hitam supaya bisa di proses ke 3
        mask = 255 - mask
        
        #proses 3
        #hasil proses 3 kan kan binary image, atau hitam dan putih nah disini dimaping tau lokasi putih2 titiknya
        connectivity = 4
        output = cv2.connectedComponentsWithStats(mask, connectivity, cv2.CV_8U)        
        num_labels = output[0]
        stats = output[2]                
        
        #ngebuat kotakan roi
        #panggil data awal "input.jpg" terus di buat COLOR_GRAY2BGR teurs dibuat kotak2 berdasar proses ke 3
        cimg = cv2.cvtColor(img,cv2.COLOR_GRAY2BGR)
        #random buat nentuin nilai warnanya
        random = Random()
        #perulangan buat ngasih kotakan sesuai jumlahnya ada berapa
        for i in range(1, num_labels):            
            #rectangel fungsi buat kotakan, nilainya dari si proses ke 3
            cv2.rectangle(cimg, (stats[i, cv2.CC_STAT_LEFT], stats[i, cv2.CC_STAT_TOP]), 
                (stats[i, cv2.CC_STAT_LEFT] + stats[i, cv2.CC_STAT_WIDTH], stats[i, cv2.CC_STAT_TOP] + stats[i, cv2.CC_STAT_HEIGHT]), random_color(random), 2)
        
        #simpan gambarnya yang ada kotak2nya
        cv2.imwrite("res.jpg", cimg)

        #baca gambar hasil proses
        img = cv2.imread('res.jpg')
        #convert img ke array pixel
        _, im_arr = cv2.imencode('.jpg', img)
        #convert array pixel ke bytes
        im_bytes = im_arr.tobytes()
        #convert bytes ke b64encode
        im_b64 = base64.b64encode(im_bytes)
        #convert b64encode ke string
        im_b64 = im_b64.decode("utf-8")

        #kirim data ke android
        num_labels = str(num_labels)
        num_labels = num_labels

        #end

        from time import localtime, strftime
        time = strftime("%Y-%m-%d %H:%M:%S", localtime())         

        cursor = mysql.connection.cursor()
        bundle = (time, im_b64, num_labels)
        cursor.execute("INSERT INTO data(time, data, status) VALUES  (%s, %s, %s)",bundle)    
        mysql.connection.commit()        
        cursor.close()
        return jsonify({"error":False,"message":"sad","res_img":str(im_b64),"many_blob":num_labels} )
    #kalo gak ada permintaan data dari android artinya error
    else:
        return jsonify({"error":True,"message":"Error","res_img":"Error","many_blob":"Error"} )


@app.route('/view', methods=["GET", "POST"])
def view():   
    global temp_id
    temp_id = 0
    temp_id = []
    temp_time = []
    temp_data = []
    temp_status = [] 
    cursor = mysql.connection.cursor()
    cursor.execute("SELECT * FROM data ORDER BY id DESC limit 5")                
    datas_list = cursor.fetchall()     
    for row in datas_list:
        print(row[3])
        temp_id.append(row[0])
        temp_time.append(row[1])
        temp_data.append(row[2])
        temp_status.append(row[3])
    cursor.close()

    return jsonify({"error":False,
    "temp_id_0":temp_id[0], "temp_id_1":temp_id[1], "temp_id_2":temp_id[2], "temp_id_3":temp_id[3], "temp_id_4":temp_id[4],
    "temp_time_0":temp_time[0], "temp_time_1":temp_time[1], "temp_time_2":temp_time[2], "temp_time_3":temp_time[3], "temp_time_4":temp_time[4],
    "temp_data_0":temp_data[0], "temp_data_1":temp_data[1], "temp_data_2":temp_data[2], "temp_data_3":temp_data[3], "temp_data_4":temp_data[4],
    "temp_status_0":temp_status[0], "temp_status_1":temp_status[1], "temp_status_2":temp_status[2], "temp_status_3":temp_status[3], "temp_status_4":temp_status[4]
    } 
    )


if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000, debug=True)
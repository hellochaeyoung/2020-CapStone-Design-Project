import socket, threading
from PIL import Image
import os
import temp
import numpy as np
import base64
import cv2

#pr = ['height', 'width', '']

class TCPServerThread(threading.Thread) :

    def __init__(self, tcpServerThreads, connections, socket, clientAddress):
        threading.Thread.__init__(self)
        
        self.tcpServerThreads = tcpServerThreads
        self.connetions = connections
        self.socket = socket
        self.clientAddress = clientAddress
    
    def run(self):
        all_msg = ""
        LEN = 1024 ** 2
        
        while all_msg[-1:] != "}":
            while True:
                b = self.socket.recv(LEN) #이미지 받기
                print(len(b))
                all_msg += b.decode('utf-8')
                if len(b) < LEN:
                    break

        new_msg=all_msg[:-1]
        
            
        try:
            
            decoded_data = base64.b64decode(new_msg)
            np_data = np.frombuffer(decoded_data,np.uint8)
            img = cv2.imdecode(np_data,cv2.IMREAD_UNCHANGED)
            img = cv2.resize(img,(1280,960))
            #img = cv2.resize(img, None, fx=0.8, fy=0.8)
            
            yolo = temp.Yolo() #yolo 객체 생성
            print("yolo 객체 생성 ok")
            image = yolo.loadImage(img)
            print("yolo load image ok")
            outs = yolo.detectingObjects(image)
            print("yolo detect objects ok")
            shapes = yolo.createPowerPoint()
            presentation = yolo.show(outs, shapes) # Yolo 객체 인식 후 결과 창, 피피티 만드는 함수
            #yolo.show(outs, shapes)
            print("ppt show ok")
            
            
            
            #여기서 결과 창 보여줄 게 아니라 생성된 피피티 파일 다시 전송 
            
            directory = "C:/Users/hansung/"
            fileName = "test.pptx"
            
            #directory = "C:/Users/hansung/Desktop/"
            #fileName = "1.jpg"
            print("size$$$$$")
            print(str(os.path.getsize(directory+fileName)))
            #3.
            with open(directory + fileName, 'rb') as f:
                print("file open in")
                fdata = "".encode()
                '''try:
                    basedata = base64.b64encode(fdata.encode('utf-8'))
                except Exception as e:
                    print(e)'''
                
                print("@@@@@@@@@@@@@@@@@@@@@")
                try:
                    data = f.read(1024)
                    #data = base64.b64encode(data)
                    print(type(data))
                    print(data)
                    while data:
                        print("in while")
                        fdata += data
                        #basedata += data
                        print("&&&&&&&&&")
                        #data = data + self.request.send(data)
                        data = f.read(1024)
                        #data = base64.b64encode(data)
                        print("@@@@@@@@")
                    #f.close()
                    print("data ok")
                    print(f)
                except Exception as e:
                    print(e)
            fdata += "}}}".encode()
            print("*********************")
            print(len(fdata))
            print(fdata)
            print(type(fdata))
            
            try:
                self.socket.send(fdata)
            except Exception as e:
                print(e)
            
            print("!!!!!!!!!!!!!!!!!!!!!")
            
            
            
            print('tcp server(client) :: exit!!')
            print('tcp server :: server wait...')
            
            
        except:
            self.connetions.remove(self.socket)
            self.tcpServerThreads.remove(self)
            print('error')
            exit(0)
        self.connetions.remove(self.socket)
        self.tcpServerThreads.remove(self)
        

    
    

                
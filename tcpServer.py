import socket, threading
import tcpServerThread


class TCPServer(threading.Thread):
    def __init__(self, HOST, PORT): #객체 생성 되는 순간 초기화해주는 메소드
        threading.Thread.__init__(self)
        
        self.HOST = HOST
        self.PORT = PORT
        
        self.serverSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.serverSocket.bind((self.HOST, self.PORT))
        self.serverSocket.listen(1)
        
        self.connections = []
        self.tcpServerThreads = [] #client마다 생성되는 스레드 저장 배열
        
    def run(self):
        try:
            while True:
                print('tcp server :: server wait...')
                socket, clientAddress = self.serverSocket.accept()
                self.connections.append(socket)
                print("tcp server :: connect :", clientAddress)
                
                #클라이언트 별로 스레드 생성
                subThread = tcpServerThread.TCPServerThread(self.tcpServerThreads, self.connections, socket, clientAddress)
                subThread.start()
                self.tcpServerThreads.append(subThread)
        except:
            print("tcp server :: serverThread error")
        
    
    def sendAll(self, message) :
        try:
            self.tcpServerThreads[0].send(message)
        except:
            pass

            
from http.server import SimpleHTTPRequestHandler, HTTPServer
import os
import json
import csv
import os.path

import NeuralNetworkModel
import ScheduledAlgo



class S(SimpleHTTPRequestHandler):
    def _set_headers(self):
        self.send_response(200)
        self.send_header('Content-type', 'application/json')
        #self.send_header('charset','utf-8')
        self.end_headers()

    def do_GET(self):
        url=self.path
        self._set_headers()
        #msg={"Success":"yes","msg":"TasksPosted"}
        #self.wfile.write((json.dumps(msg)).encode())
        #msg="<html><body><h1>POST data successfull!</h1></body></html>"
        #self.wfile.write(msg.encode())

        print(url)
        
        if(url=='/run_tensorflow'):
            msg={"Success":"yes"}
            self.wfile.write((json.dumps(msg)).encode())   
            NeuralNetworkModel.NNModel()

        if(url=='/schedule_tasks'):
            msg={"Success":"yes"}
            #self.wfile.write((json.dumps(msg)).encode())   
            ScheduledAlgo.schedule_algo()
            json_data=open("scheduled_Task.json").read()
            data = json.loads(json_data)
            self.wfile.write((json.dumps(data)).encode())   
            print(data)
        
    
        
    def do_HEAD(self):
        self._set_headers()

    def do_POST(self):
        url=self.path
        print(url)
        if(url=='/post_free_time'):
            # Doesn't do anything with posted data
            data={}
            self._set_headers()
            content_length = int(self.headers['Content-Length']) # <--- Gets the size of data
            post_data = self.rfile.read(content_length) # <--- Gets the data itself      
            #msg="<html><body><h1>POST data successfull!</h1></body></html>"
            #self.wfile.write(msg.encode())
            
            post_data=post_data.decode("utf-8")
            post_data=post_data.split("&")
            for i in post_data:
                i=i.split("=")
                data[i[0]]=i[1]
            print(data)
            keys=[]
            for x in data.keys():
                keys.append(x)

            print(keys)
            fileEmpty = os.stat('free_time.csv').st_size == 0
            with open('free_time.csv', 'a', newline='') as output_file:

                headers = ['Free','hour','minu','weekday']
                dict_writer = csv.DictWriter(output_file, fieldnames=headers)
                
                if fileEmpty:
                    dict_writer.writeheader()
                
                
                   
                dict_writer.writerow({'Free':data['Free'],'hour':data['hour'],'minu':data['minu'],'weekday':data['weekday']})
            msg={"Success":"yes","msg":"freetimePosted"}
            self.wfile.write((json.dumps(msg)).encode())
  

        if(url=='/post_tasks'):
            # Doesn't do anything with posted data
            data={}
            self._set_headers()
            content_length = int(self.headers['Content-Length']) # <--- Gets the size of data
            post_data = self.rfile.read(content_length) # <--- Gets the data itself      
            #msg="<html><body><h1>POST Task successfull!</h1></body></html>"
            #self.wfile.write(msg.encode())
        
            post_data=post_data.decode("utf-8")
            post_data=post_data.split("&")
            for i in post_data:
                i=i.split("=")
                data[i[0]]=i[1]
            print(data)
            keys=[]
            for x in data.keys():
                keys.append(x)

            print(keys)
            with open('tasks.csv', 'w', newline='') as output_file:

                headers = ['Task','hour']
                dict_writer = csv.DictWriter(output_file, fieldnames=headers)
                dict_writer.writeheader()
                for x in data.keys():
                    dict_writer.writerow({'Task':x,'hour':data[x]})
            msg={"Success":"yes","msg":"TasksPosted"}
            self.wfile.write((json.dumps(msg)).encode())
        
    
def run(server_class=HTTPServer, handler_class=S, port=7880):
    server_address = ('', port)
    httpd = server_class(server_address, handler_class)
    print('Starting httpd...7880')
    httpd.serve_forever()

if __name__ == "__main__":
    from sys import argv

    if len(argv) == 2:
        run(port=int(argv[1]))
    else:
        run()

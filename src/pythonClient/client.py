import grpc
import pingpong_pb2
import pingpong_pb2_grpc

def run():
    channel = grpc.insecure_channel('localhost:8080')
    stub = pingpong_pb2_grpc.PingPongServiceStub(channel)
    response = stub.SendPing(pingpong_pb2.PingRequest(message='Ping'))
    print("Response:", response.message)

if __name__ == '__main__':
    run()
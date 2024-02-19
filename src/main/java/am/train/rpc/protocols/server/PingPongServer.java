package am.train.rpc.protocols.server;

import am.train.rpc.protocols.stubs.PingPongServiceGrpc;
import am.train.rpc.protocols.stubs.PingRequest;
import am.train.rpc.protocols.stubs.PongResponse;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PingPongServer {
    private Server server;
    private static final Logger LOGGER = LoggerFactory.getLogger(PingPongServer.class);

    private void start() throws IOException {
        int port = 8080;
        server = ServerBuilder.forPort(port)
                .addService(new PingPongServiceImpl())
                .build()
                .start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("*** shutting down gRPC server since JVM is shutting down");
            PingPongServer.this.stop();
            LOGGER.info("*** server shut down");
        }));
        LOGGER.info("*** server started at the port 8080");
    }

    private void stop() {
        if (server != null) {
            LOGGER.info("*** shutting down the server");
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final PingPongServer server = new PingPongServer();
        server.start();
        server.blockUntilShutdown();
    }

    static class PingPongServiceImpl extends PingPongServiceGrpc.PingPongServiceImplBase {
        @Override
        public void sendPing(PingRequest req, StreamObserver<PongResponse> responseObserver) {
            PongResponse response = PongResponse.newBuilder().setMessage("Pong").build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}

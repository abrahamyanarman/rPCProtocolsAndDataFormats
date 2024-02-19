package am.train.rpc.protocols.client;

import am.train.rpc.protocols.stubs.PingPongServiceGrpc;
import am.train.rpc.protocols.stubs.PingRequest;
import am.train.rpc.protocols.stubs.PongResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPongClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(PingPongClient.class);

    public static void main(String[] args) throws Exception {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        PingPongServiceGrpc.PingPongServiceBlockingStub stub = PingPongServiceGrpc.newBlockingStub(channel);

        PingRequest request = PingRequest.newBuilder().setMessage("Ping").build();
        LOGGER.info("Sending request: {}", request.getMessage());
        PongResponse response = stub.sendPing(request);
        LOGGER.info("Response: " + response.getMessage());

        channel.shutdownNow();
    }
}

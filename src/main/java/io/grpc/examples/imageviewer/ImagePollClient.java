package io.grpc.examples.imageviewer;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

//class ImageDataSample {
//    public String imageName;
//    public boolean isNegative;
//    public boolean isNeutral;
//    public boolean isPositive;
//    public boolean isBlackScreen;
//
//    public ImageDataSample(String imageName, boolean isNegative, boolean isNeutral, boolean isPositive, boolean isBlackScreen) {
//        this.imageName = imageName;
//        this.isNegative = isNegative;
//        this.isNeutral = isNeutral;
//        this.isPositive = isPositive;
//        this.isBlackScreen = isBlackScreen;
//    }
//}

public class ImagePollClient {
    private static final Logger logger = Logger.getLogger(ImagePollClient.class.getName());

    private final ManagedChannel channel;
    private final SendingImageDataGrpc.SendingImageDataBlockingStub blockingStub;

    public ImagePollClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext(true)
                .build());
    }

    ImagePollClient(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = SendingImageDataGrpc.newBlockingStub(channel);
        System.out.println("sent message");
    }

    public void packImageData(ImageDataSample imageData){
        logger.info("Will try pack data to canal...");

        ImageDataMessage imageCanal = ImageDataMessage.newBuilder()
                .setImageName(ByteString.copyFromUtf8(imageData.imageName))
                .setNegative(imageData.isNegative)
                .setNeutral(imageData.isNeutral)
                .setPositive(imageData.isPositive)
                .setBlackScreen(imageData.isBlackScreen)
                .build();
        ServerConfirmation confirmation;
        try {
            logger.info("I will send packet...");
            confirmation = blockingStub.sendImageData(imageCanal);
            logger.info("Packet was sent successful");
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Confirmation: " + confirmation.getConfirm().toStringUtf8());
    }


    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public static void main(String[] args) throws Exception {
        ImagePollClient client = new ImagePollClient("localhost", 50051);
        ImageDataSample[] imageList = new ImageDataSample[5];
        imageList[0] = new ImageDataSample("A098", true, false, false,false);
        imageList[1] = new ImageDataSample("A028", false, false, false,false);
        imageList[2] = new ImageDataSample("Ne098", true, false, false,false);
        imageList[3] = new ImageDataSample("P098", false, false, true,false);
        imageList[4] = new ImageDataSample("Black", false, false, false,true);

        try {
            /* Access a service running on the local machine on port 50051 */
//            if (args.length > 0) {
//                imageName = args[0]; /* Use the arg as the name to greet if provided */
//            }
            for (ImageDataSample data : imageList)
                client.packImageData(data);
            sleep(5000);
        }catch (InterruptedException e){
            e.printStackTrace();
        } finally {
            client.shutdown();
        }
    }
}
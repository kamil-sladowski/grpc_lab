package io.grpc.examples.imageviewer;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;

public class SendingImageDataImpl extends SendingImageDataGrpc.SendingImageDataImplBase {

    @Override
    public void sendImageData(ImageDataMessage request, StreamObserver<ServerConfirmation> responseObserver) {
        String replyVal = new String("1");
        ServerConfirmation reply = ServerConfirmation.newBuilder().setConfirm(ByteString.copyFromUtf8(replyVal)).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}

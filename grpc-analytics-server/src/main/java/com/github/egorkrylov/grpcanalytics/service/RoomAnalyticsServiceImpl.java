package com.github.egorkrylov.grpcanalytics.service;

import com.github.egorkrylov.grpc.AnalyzeRoomRequest;
import com.github.egorkrylov.grpc.AnalyzeRoomResponse;
import com.github.egorkrylov.grpc.RoomAnalyticsGrpc;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoomAnalyticsServiceImpl extends RoomAnalyticsGrpc.RoomAnalyticsImplBase {

    private static final Logger log = LoggerFactory.getLogger(RoomAnalyticsServiceImpl.class);

    @Override
    public void analyzeRoom(AnalyzeRoomRequest request, StreamObserver<AnalyzeRoomResponse> responseObserver) {
        log.info("gRPC запрос: анализ номера id={} (тип: {}, начальная цена: {})",
                request.getRoomId(), request.getRoomType(), request.getPrice());

        double finalPrice = costEstimation(request.getRoomType(), request.getPrice());
        String classification = classificationPrice(finalPrice);
        double priceWithDisc = discountCalculation(finalPrice);

        AnalyzeRoomResponse roomResponse = AnalyzeRoomResponse.newBuilder()
                .setRoomId(request.getRoomId())
                .setFinalPrice(finalPrice)
                .setPriceCategory(classification)
                .setDiscountPrice(priceWithDisc)
                .build();

        log.info("gPRC ответ: номер id={}, итоговая цена={} руб, категория={}, цена со скидкой={}",
                roomResponse.getRoomId(), finalPrice, classification, priceWithDisc);

        responseObserver.onNext(roomResponse);
        responseObserver.onCompleted();
    }

    private double costEstimation(String roomType, double price) {
        return switch (roomType) {
            case "Эконом" -> price * 1.0;
            case "Стандарт" -> price * 1.1;
            case "Улучешный" -> price * 1.2;
            case "Люкс" -> price * 1.3;
            case "Президентский" -> price * 1.5;
            default -> price;
        };
    }

    private String classificationPrice(double finalPrice) {
        if (finalPrice < 3000) return "Эконом";
        if (finalPrice >= 3000 && finalPrice < 6000) return "Комфорт";
        if (finalPrice >= 6000 && finalPrice < 12000) return "Бизнес";
        return "Премиум";
    }

    private double discountCalculation(double finalPrice) {
        if(finalPrice > 20000) return finalPrice * 0.75;
        if(finalPrice > 15000) return finalPrice * 0.85;
        if(finalPrice > 10000) return finalPrice * 0.9;
        return finalPrice;
    }


}

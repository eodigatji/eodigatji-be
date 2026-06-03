package eodigatji.eodigatjiserver.mypage.dto;

public class MyPageTemperatureResponseDto {

    private final Integer temperature;

    public MyPageTemperatureResponseDto(Integer temperature) {
        this.temperature = temperature;
    }

    public Integer getTemperature() {
        return temperature;
    }
}
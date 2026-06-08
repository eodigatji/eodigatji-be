package eodigatji.eodigatjiserver.mypage.dto;

public class MyPageResponseDto {

    private String email;
    private String nickname;
    private String studentNumber;
    private Integer temperature;

    public MyPageResponseDto(
            String email,
            String nickname,
            String studentNumber,
            Integer temperature
    ) {
        this.email = email;
        this.nickname = nickname;
        this.studentNumber = studentNumber;
        this.temperature = temperature;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public Integer getTemperature() {
        return temperature;
    }
}
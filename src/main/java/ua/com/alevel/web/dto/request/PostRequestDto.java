package ua.com.alevel.web.dto.request;

import org.springframework.web.multipart.MultipartFile;

public class PostRequestDto extends RequestDto {

    private MultipartFile multipartFile;
    private String nameGame;
    private String emailUser;

    public PostRequestDto() {
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public String getNameGame() {
        return nameGame;
    }

    public void setNameGame(String nameGame) {
        this.nameGame = nameGame;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }
}

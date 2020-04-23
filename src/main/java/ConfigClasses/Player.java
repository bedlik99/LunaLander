package ConfigClasses;

public class Player {

    private String nickName;
    private double gamingResult;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Player(String nickName, double gamingResult){
        this.nickName = nickName;
        this.gamingResult = gamingResult;
    }


    public double getGamingResult() {
        return gamingResult;
    }

    public void setGamingResult(double gamingResult) {
        this.gamingResult = this.gamingResult + gamingResult;
    }

}

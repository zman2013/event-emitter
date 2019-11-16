import com.zman.event.EventEmitter;

public class Airplane extends EventEmitter {

    public void takeoff(){
        emit("takeoff");
    }

    public void flying(int speed){
        emit("speed", speed);
    }

    public void landing(){
        emit("landing");
    }

    public static void main(String[] args){
        Airplane airplane = new Airplane();

        airplane.on("takeoff", d -> System.out.println("takeoff"));
        airplane.on("speed",   d -> System.out.println("speed: "+d));
        airplane.on("landing", d -> System.out.println("landing"));

        airplane.takeoff();
        airplane.flying(300);
        airplane.flying(600);
        airplane.flying(1000);
        airplane.flying(600);
        airplane.flying(300);
        airplane.landing();
    }

}

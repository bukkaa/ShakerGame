package bukkaa.mediahouse.ShakerGameWS.model;

/**
 * Created by bukkaa on 11.04.2017.
 */
public class GamerAccelerationData {
    private int id;
    private float ax;
    private float ay;


    public GamerAccelerationData(int id, float ax, float ay) {
        this.id = id;
        this.ax = ax;
        this.ay = ay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getAx() {
        return ax;
    }

    public void setAx(float ax) {
        this.ax = ax;
    }

    public float getAy() {
        return ay;
    }

    public void setAy(float ay) {
        this.ay = ay;
    }

    @Override
    public String toString() {
        return "{id: <" + id +
                ">, ax: " + ax +
                ", ay: " + ay + "}";
    }
}

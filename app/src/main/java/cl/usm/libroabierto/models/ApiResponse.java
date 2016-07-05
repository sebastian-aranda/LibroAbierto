package cl.usm.libroabierto.models;

import org.json.JSONArray;

public class ApiResponse {
    int state;
    String msg;

    public ApiResponse(int state, String msg) {
        this.state = state;
        this.msg = msg;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "state=" + state +
                ", msg='" + msg + '\'' +
                '}';
    }
}

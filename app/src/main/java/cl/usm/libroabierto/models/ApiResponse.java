package cl.usm.libroabierto.models;

import org.json.JSONArray;

public class ApiResponse {
    Integer state;
    String msg;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ApiResponse(Integer state, String msg) {
        this.state = state;
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

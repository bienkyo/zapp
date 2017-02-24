package vn.com.z11.z11app.ApiResponseModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kienlv58 on 12/19/16.
 */
public class RegisterResponse implements Serializable{
    @SerializedName("code")
    public int code;
    @SerializedName("status")
    public String status;
    @SerializedName("metadata")
    public Metadata metadata;

    public static class User {
        @SerializedName("name")
        public String name;
        @SerializedName("email")
        public String email;

        public User(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class Metadata {
        @SerializedName("user")
        public User user;

        public Metadata(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

}

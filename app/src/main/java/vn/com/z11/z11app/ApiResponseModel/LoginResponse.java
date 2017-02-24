package vn.com.z11.z11app.ApiResponseModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kienlv58 on 12/6/16.
 */
public class LoginResponse implements Serializable {
    @SerializedName("code")
    private int code;
    @SerializedName("status")
    private String status;
    @SerializedName("metadata")
    private Metadata metadata = null;

    public LoginResponse(int code, String status, Metadata metadata) {
        this.code = code;
        this.status = status;
        this.metadata = metadata;
    }

    public int getCode() {

        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public static class Metadata implements Serializable{
        @SerializedName("user")
        private User user;
        @SerializedName("token")
        private String token;

        public Metadata(User user, String token) {
            this.user = user;
            this.token = token;
        }

        public User getUser() {

            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public static class User implements Serializable{
            @SerializedName("id")
            private int id;
            @SerializedName("email")
            private String email;
            @SerializedName("active")
            private int active;
            @SerializedName("grant_type")
            private String grant_type;
            @SerializedName("profile")
            private Profile profile;

            public  User(int id, String email, int active, String grant_type, Profile profile) {
                this.id = id;
                this.email = email;
                this.active = active;
                this.grant_type = grant_type;
                this.profile = profile;
            }

            public int getId() {

                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public int getActive() {
                return active;
            }

            public void setActive(int active) {
                this.active = active;
            }

            public String getGrant_type() {
                return grant_type;
            }

            public void setGrant_type(String grant_type) {
                this.grant_type = grant_type;
            }

            public Profile getProfile() {
                return profile;
            }

            public void setProfile(Profile profile) {
                this.profile = profile;
            }

            public static class Profile implements Serializable {
                @SerializedName("image")
                private String image;
                @SerializedName("name")
                private String name;
                @SerializedName("gender")
                private String gender;
                @SerializedName("coin")
                private int coin;

                public Profile(String image, String name, String gender, int coin) {
                    this.image = image;
                    this.name = name;
                    this.gender = gender;
                    this.coin = coin;
                }

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getGender() {
                    return gender;
                }

                public void setGender(String gender) {
                    this.gender = gender;
                }

                public int getCoin() {
                    return coin;
                }

                public void setCoin(int coin) {
                    this.coin = coin;
                }
            }
        }


    }


}

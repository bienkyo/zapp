package vn.com.z11.z11app.ApiResponseModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Bien-kun on 21/02/2017.
 */

public class LanguageResponse extends BaseResponse {

    @SerializedName("listlanguage")
    public ArrayList<Language> languages;

    public static class Language {
        @SerializedName("language_id")
        public int id;
        @SerializedName("language_code")
        public String languageCode;
        @SerializedName("description")
        public String description;

        public Language(){

        }

        public Language(int id, String languageCode, String description) {
            this.id = id;
            this.languageCode = languageCode;
            this.description = description;
        }
    }
}

package vn.com.z11.z11app.ApiResponseModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kienlv58 on 12/23/16.
 */
public class PackageResponse implements Serializable{


    @SerializedName("code")
    public int code;
    @SerializedName("status")
    public String status;
    @SerializedName("metadata")
    public Package mPackage;

    public static class Translate_name_text implements Serializable{
        @SerializedName("translate_id")
        public int translate_id;
        @SerializedName("text_id")
        public int text_id;
        @SerializedName("language_code")
        public String language_code;
        @SerializedName("text_value")
        public String text_value;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("updated_at")
        public String updated_at;
    }

    public static class Translate_describe_text implements Serializable{
        @SerializedName("translate_id")
        public int translate_id;
        @SerializedName("text_id")
        public int text_id;
        @SerializedName("language_code")
        public String language_code;
        @SerializedName("text_value")
        public String text_value;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("updated_at")
        public String updated_at;
    }

    public static class Groupquestion implements Serializable{
        @SerializedName("group_question_id")
        public int group_question_id;
        @SerializedName("item_code")
        public String item_code;
        @SerializedName("chapter_id")
        public int chapter_id;
        @SerializedName("explain_item_id")
        public int explain_item_id;
        @SerializedName("group_question_content")
        public String group_question_content;
        @SerializedName("group_question_transcript")
        public String group_question_transcript;
        @SerializedName("group_question_image")
        public String group_question_image;
        @SerializedName("group_question_audio")
        public String group_question_audio;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("updated_at")
        public String updated_at;
    }

    public static class Chapters implements Serializable{
        @SerializedName("chapter_id")
        public int chapter_id;
        @SerializedName("item_code")
        public String item_code;
        @SerializedName("package_id")
        public int package_id;
        @SerializedName("name_text")
        public String name_text;
        @SerializedName("describe_text")
        public String describe_text;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("updated_at")
        public String updated_at;
        @SerializedName("groupquestion")
        public List<Groupquestion> groupquestion;
    }

    public static class Package {
        @SerializedName("package_id")
        public int package_id;
        @SerializedName("item_code")
        public String item_code;
        @SerializedName("folder_id")
        public int folder_id;
        @SerializedName("name_text_id")
        public int name_text_id;
        @SerializedName("describe_text_id")
        public int describe_text_id;
        @SerializedName("approval")
        public int approval;
        @SerializedName("package_cost")
        public int package_cost;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("updated_at")
        public String updated_at;
        @SerializedName("balance_rate")
        public int balance_rate;
        @SerializedName("count_user_rate")
        public int count_user_rate;
        @SerializedName("translate_name_text")
        public List<Translate_name_text> translate_name_text;
        @SerializedName("translate_describe_text")
        public List<Translate_describe_text> translate_describe_text;
        @SerializedName("chapters")
        public List<Chapters> chapters;
    }
}

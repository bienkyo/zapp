package vn.com.z11.z11app.ApiResponseModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kienlv58 on 12/3/16.
 */
public class CategoryResponse implements Serializable{

    @SerializedName("code")
    public int code;
    @SerializedName("status")
    public String status;
    @SerializedName("metadata")
    public List<Category> list_category;

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


    public static class Packages implements Serializable{
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
    }

    public static class Folder implements Serializable{
        @SerializedName("folder_id")
        public int folder_id;
        @SerializedName("item_code")
        public String item_code;
        @SerializedName("category_id")
        public int category_id;
        @SerializedName("name_text_id")
        public int name_text_id;
        @SerializedName("describe_text_id")
        public int describe_text_id;
        @SerializedName("owner_id")
        public int owner_id;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("updated_at")
        public String updated_at;
        @SerializedName("translate_name_text")
        public List<Translate_name_text> translate_name_text;
        @SerializedName("translate_describe_text")
        public List<Translate_describe_text> translate_describe_text;
        @SerializedName("packages")
        public List<Packages> packages;
    }

    public static class Category {
        @SerializedName("category_id")
        public int category_id;
        @SerializedName("category_code")
        public String category_code;
        @SerializedName("image")
        public String image;
        @SerializedName("name_text_id")
        public int name_text_id;
        @SerializedName("describe_text_id")
        public int describe_text_id;
        @SerializedName("creator_id")
        public int creator_id;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("updated_at")
        public String updated_at;
        @SerializedName("translate_name_text")
        public List<Translate_name_text> translate_name_text;
        @SerializedName("translate_describe_text")
        public List<Translate_describe_text> translate_describe_text;
        @SerializedName("folder")
        public List<Folder> folder;
    }
}

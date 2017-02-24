package vn.com.z11.z11app.ApiResponseModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kienlv58 on 12/29/16.
 */
public class ChapterResponse implements Serializable{

    @SerializedName("code")
    public int code;
    @SerializedName("status")
    public String status;
    @SerializedName("metadata")
    public Chapter chapter;

    public static class Answers implements Serializable{
        @SerializedName("answer_item_id")
        public int answer_item_id;
        @SerializedName("item_code")
        public String item_code;
        @SerializedName("question_id")
        public int question_id;
        @SerializedName("answer_item_value")
        public String answer_item_value;
        @SerializedName("answer_is_correct")
        public int answer_is_correct;
    }

    public static class Questions implements Serializable{
        @SerializedName("question_id")
        public int question_id;
        @SerializedName("item_code")
        public String item_code;
        @SerializedName("group_question_id")
        public int group_question_id;
        @SerializedName("sub_question_content")
        public String sub_question_content;
        @SerializedName("explain_item_id")
        public int explain_item_id;
        @SerializedName("answers")
        public List<Answers> answers;
    }

    public static class GroupQS implements Serializable{
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
        @SerializedName("questions")
        public List<Questions> questions;
    }

    public static class Chapter implements Serializable{
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
        @SerializedName("groupQS")
        public List<GroupQS> groupQS;
    }
}

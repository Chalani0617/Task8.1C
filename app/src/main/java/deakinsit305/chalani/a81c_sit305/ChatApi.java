package deakinsit305.chalani.a81c_sit305;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ChatApi {
    @FormUrlEncoded
    @POST("/chat")
    Call<ResponseBody> getReply(@Field("userMessage") String userMessage);
}

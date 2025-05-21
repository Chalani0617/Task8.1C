package deakinsit305.chalani.a81c_sit305;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ChatActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText etMessage;
    Button btnSend;
    List<Message> messageList = new ArrayList<>();
    MessageAdapter adapter;
    ChatApi chatApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recyclerView);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);

        adapter = new MessageAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new MessageItemAnimator());

        OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(900, TimeUnit.SECONDS)
            .readTimeout(900, TimeUnit.SECONDS)
            .writeTimeout(900, TimeUnit.SECONDS)
            .build();

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        chatApi = retrofit.create(ChatApi.class);

        String username = getIntent().getStringExtra("username");
        addMessage("Welcome " + username + "!", false);

        btnSend.setOnClickListener(v -> {
            String userText = etMessage.getText().toString().trim();
            if (!userText.isEmpty()) {
                addMessage(userText, true);
                etMessage.setText("");

                Message typingMessage = new Message("Bot is thinking...", false);
                messageList.add(typingMessage);
                adapter.notifyItemInserted(messageList.size() - 1);
                recyclerView.scrollToPosition(messageList.size() - 1);

                chatApi.getReply(userText).enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        removeTypingIndicator();

                        try {
                            if (response.isSuccessful() && response.body() != null) {
                                String reply = response.body().string();
                                addMessage(reply, false);
                            } else {
                                addMessage("Failed to get response", false);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            addMessage("Error parsing response", false);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                        removeTypingIndicator();
                        addMessage("Error: " + t.getLocalizedMessage(), false);
                    }
                });
            }
        });
    }

    void addMessage(String text, boolean isUser) {
        runOnUiThread(() -> {
            messageList.add(new Message(text, isUser));
            adapter.notifyItemInserted(messageList.size() - 1);
            recyclerView.scrollToPosition(messageList.size() - 1);
        });
    }

    void removeTypingIndicator() {
        runOnUiThread(() -> {
            if (!messageList.isEmpty() && messageList.get(messageList.size() - 1).text.equals("Bot is thinking...")) {
                messageList.remove(messageList.size() - 1);
                adapter.notifyItemRemoved(messageList.size());
            }
        });
    }
}

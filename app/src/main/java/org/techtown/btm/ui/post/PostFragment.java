package org.techtown.btm.ui.post;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.btm.R;
import org.techtown.btm.databinding.FragmentPostBinding;
import org.techtown.btm.ui.post.DetailActivity;
import org.techtown.btm.ui.post.RegisterActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class PostFragment extends Fragment {

    private FragmentPostBinding binding;

    private ListView listView;
    private Button regButton;
    private String userId;
    private ArrayList<String> titleList = new ArrayList<>();
    private ArrayList<String> seqList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        setContentView(R.layout.fragment_post);
        final String[] mid = {"네이버","다음","구글","티스토리","계발에서 개발까지"};

        ListView list = listView.findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireActivity(), android.R.layout.simple_list_item_1, mid);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(requireContext(), mid[position], Toast.LENGTH_SHORT).show();
            }
        });
        PostViewModel postViewModel =
                new ViewModelProvider(this).get(PostViewModel.class);

        binding = FragmentPostBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listView = root.findViewById(R.id.listView);
        regButton = root.findViewById(R.id.reg_button);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(requireContext(), adapterView.getItemAtPosition(i) + " 클릭", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireContext(), DetailActivity.class);
                intent.putExtra("board_seq", seqList.get(i));
                intent.putExtra("userid", userId);
                startActivity(intent);
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), RegisterActivity.class);
                intent.putExtra("userid", userId);
                startActivity(intent);
            }
        });

        return root;
    }

    private void setContentView(int fragment_post) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        GetBoard getBoard = new GetBoard();
        getBoard.execute();
    }

    class GetBoard extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("GetBoard", "onPreExecute");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("GetBoard", "onPostExecute, " + result);

            titleList.clear();
            seqList.clear();

            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.optString("title");
                    String seq = jsonObject.optString("seq");
                    titleList.add(title);
                    seqList.add(seq);
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, titleList);
                listView.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String serverUrl = "http://15.164.252.136/load_board.php";
            URL url;
            String response = "";
            try {
                url = new URL(serverUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("userid", userId);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } else {
                    response = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }
}

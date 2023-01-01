package com.example.recforrest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.recforrest.model.Model;
import com.example.recforrest.model.Recommendation;
import java.util.List;

public class RecommendationsListFragment extends Fragment {

    List<Recommendation> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recommendations_list, container, false);
        data = Model.instance().getAllRecommendations();
        RecyclerView rl = view.findViewById(R.id.recommendationsListFrag_list);
        rl.setHasFixedSize(true);

        rl.setLayoutManager(new LinearLayoutManager(getContext()));
        RecommendationRecyclerAdapter adapter = new RecommendationRecyclerAdapter(getLayoutInflater(), data);
        rl.setAdapter(adapter);

//        adapter.setListener(new RecommendationRecyclerAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int pos) {
//                Log.d("TAG", "row click " + pos);
//                Student s = data.get(pos);
//                StudentsListFragmentDirections.ActionStudentsListFragmentToBlueFragment action = StudentsListFragmentDirections.actionStudentsListFragmentToBlueFragment(s.getName());
//                Navigation.findNavController(view).navigate(action);
//            }
//        });
        return view;
    }
}

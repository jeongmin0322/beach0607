package org.techtown.btm.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.techtown.btm.R;
import org.techtown.btm.databinding.FragmentHomeBinding;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    ViewPager2 viewPager2;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewPager2 = root.findViewById(R.id.pager);
        FragmentSlidePagerAdapter fragmentSlideAdapter = new FragmentSlidePagerAdapter(getActivity());

        Fragment1 fragment1 = new Fragment1();
        Fragment2 fragment2 = new Fragment2();
        Fragment3 fragment3 = new Fragment3();

        fragmentSlideAdapter.addFragmentItem(fragment1);
        fragmentSlideAdapter.addFragmentItem(fragment2);
        fragmentSlideAdapter.addFragmentItem(fragment3);

        viewPager2.setAdapter(fragmentSlideAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class FragmentSlidePagerAdapter extends FragmentStateAdapter {

        ArrayList<Fragment> fragmentItems = new ArrayList<Fragment>();

        public FragmentSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        public void addFragmentItem(Fragment fragmentItem) {
            fragmentItems.add(fragmentItem);
        }

        // 지정된 위치와 연결된 새 프래그먼트를 보여주도록 한다
        @Override
        public Fragment createFragment(int position) {
            return fragmentItems.get(position);
        }

        // 어댑터에서 만들 페이지 수를 반환한다
        @Override
        public int getItemCount() {
            return fragmentItems.size();
        }
    }
}

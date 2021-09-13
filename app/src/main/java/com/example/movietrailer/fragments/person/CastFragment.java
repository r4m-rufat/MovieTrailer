package com.example.movietrailer.fragments.person;

import static com.example.movietrailer.utils.constants.ConstantsKt.IMAGE_BEGIN_URL;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movietrailer.R;
import com.example.movietrailer.adapters.detail_page.HorizontalCastImagesAdapter;
import com.example.movietrailer.internal_storage.PreferenceManager;
import com.example.movietrailer.models.person.cast_detail.CastDetailResponse;
import com.example.movietrailer.models.person.cast_images.CastImagesResponse;
import com.example.movietrailer.utils.check_connection.CheckConnectionAsynchronously;
import com.example.movietrailer.viewmodels.persons.CastDetailFragmentViewModel;
import com.github.ybq.android.spinkit.SpinKitView;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class CastFragment extends Fragment {

    private TextView txt_department, txt_castName, txt_castBiography, txt_castBirthday, txt_images;
    private ImageView cast_image, backIcon;
    private CastDetailFragmentViewModel viewModel;
    private int castID;
    private Context context;
    private RecyclerView recyclerCastImages;
    private HorizontalCastImagesAdapter castImagesAdapter;
    private SpinKitView progressBar;
    private LinearLayout linearCast;
    private PreferenceManager preferenceManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // showing Bottom Navigation View
        requireActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);

        viewModel = new ViewModelProvider(this).get(CastDetailFragmentViewModel.class);
        context = requireContext();
        if (getArguments() != null) {
            castID = getArguments().getInt("castID");
        }
        preferenceManager = new PreferenceManager(context);
        if (preferenceManager.getBoolean("dark_mode")) {
            context.setTheme(R.style.AppTheme_Base_Night);
        } else {
            context.setTheme(R.style.Theme_MovieTrailer);
        }
        CheckConnectionAsynchronously.INSTANCE.init(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cast, container, false);

        initializeWidgets(view);
        setResultsToWidgets();
        setupCastImagesRecycler();
        clickedBackIcon();

        return view;
    }

    private void initializeWidgets(View view) {

        txt_department = view.findViewById(R.id.txt_department);
        txt_castName = view.findViewById(R.id.castName);
        txt_castBiography = view.findViewById(R.id.txt_cast_biography);
        txt_castBirthday = view.findViewById(R.id.txt_cast_born);
        cast_image = view.findViewById(R.id.cast_image);
        backIcon = view.findViewById(R.id.ic_backFromCast);
        recyclerCastImages = view.findViewById(R.id.recyclerCastImages);
        progressBar = view.findViewById(R.id.circularProgressBar);
        linearCast = view.findViewById(R.id.linearCast);
        txt_images = view.findViewById(R.id.txt_images);

    }

    private void setResultsToWidgets() {

        linearCast.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        CheckConnectionAsynchronously.INSTANCE.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean connection) {
                if (connection) {
                    viewModel.getCastDetail(castID).observe(getViewLifecycleOwner(), new Observer<CastDetailResponse>() {
                        @Override
                        public void onChanged(CastDetailResponse castDetailResponse) {
                            viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
                                if (!loading) {
                                    txt_department.setText(castDetailResponse.getKnownForDepartment());
                                    txt_castName.setText(castDetailResponse.getName());
                                    if (!castDetailResponse.getBiography().equals("")) {
                                        txt_castBiography.setText(castDetailResponse.getBiography());
                                    } else {
                                        txt_castBiography.setText(getString(R.string.biography_error));
                                    }
                                    String born = "";
                                    if (castDetailResponse.getBirthday() != null) {
                                        born += castDetailResponse.getBirthday();
                                        if (castDetailResponse.getPlaceOfBirth() != null) {
                                            born += (" - Place: " + castDetailResponse.getPlaceOfBirth());
                                        }
                                    } else {
                                        born = getString(R.string.biography_error);
                                    }
                                    txt_castBirthday.setText(born);
                                    if (castDetailResponse.getProfilePath() != null) {
                                        Glide.with(context).load(IMAGE_BEGIN_URL + castDetailResponse.getProfilePath()).into(cast_image);
                                    } else {
                                        cast_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_person));
                                    }
                                    progressBar.setVisibility(View.GONE);
                                    linearCast.setVisibility(View.VISIBLE);
                                }

                            });
                        }
                    });
                }
            }
        });

    }

    private void setupCastImagesRecycler() {

        recyclerCastImages.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerCastImages.setHasFixedSize(true);
        castImagesAdapter = new HorizontalCastImagesAdapter(context);

        if (viewModel.getImages(castID).getValue() == null) {
            txt_images.setVisibility(View.GONE);
        }

        CheckConnectionAsynchronously.INSTANCE.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean connection) {
                if (connection) {
                    viewModel.getImages(castID).observe(getViewLifecycleOwner(), new Observer<CastImagesResponse>() {
                        @Override
                        public void onChanged(CastImagesResponse castImagesResponse) {
                            if (castImagesResponse.getProfiles().size() != 0){
                                txt_images.setVisibility(View.VISIBLE);
                            }
                            castImagesAdapter.updateCastImageList(castImagesResponse);
                        }
                    });
                }
            }
        });

        recyclerCastImages.setAdapter(castImagesAdapter);
    }

    private void clickedBackIcon() {
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack(R.id.viewFilmDetailFragment, false);
            }
        });
    }

}
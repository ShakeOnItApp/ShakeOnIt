package com.dde.shakeonit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewDealViewFragment extends Fragment {

    public NewDealViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View fragmentLayout = inflater.inflate(R.layout.fragment_new_deal_view, container, false);
        Button mButton = (Button) fragmentLayout.findViewById(R.id.newDealSubmitButton);
        final EditText mTitleEntry = (EditText) fragmentLayout.findViewById(R.id.newDealTitle);
        final EditText mTextEntry = (EditText) fragmentLayout.findViewById(R.id.newDealText);
        final EditText mLocationEntry = (EditText) fragmentLayout.findViewById(R.id.newDealLocation);
        final EditText mParticipantsEntry = (EditText) fragmentLayout.findViewById(R.id.newDealParticipants);
        final EditText mTermsEntry = (EditText) fragmentLayout.findViewById(R.id.newDealTerms);

        mButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View view){
                        String dealTitle = mTitleEntry.getText().toString();
                        String dealDescription = mTextEntry.getText().toString();
                        String dealLocation = mLocationEntry.getText().toString();
                        String dealParticipants = mParticipantsEntry.getText().toString();
                        String dealTerms = mTermsEntry.getText().toString();

                        PostNewDeal postNewDeal = new PostNewDeal(getContext());
                        postNewDeal.execute( dealParticipants, dealTitle, dealDescription, dealTerms, dealLocation);

                        getActivity().finish();
                    }
                }
        );


        // return the inflated view
        return fragmentLayout;
    }
}

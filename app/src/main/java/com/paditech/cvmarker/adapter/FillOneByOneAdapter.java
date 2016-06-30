package com.paditech.cvmarker.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.paditech.cvmarker.fragment.EducationFragment;
import com.paditech.cvmarker.fragment.ExperienceFragment;
import com.paditech.cvmarker.fragment.ObjectiveFragment;
import com.paditech.cvmarker.fragment.OtherSectionFragment;
import com.paditech.cvmarker.fragment.PersonalInfoFragment;
import com.paditech.cvmarker.fragment.SkillsFragment;
import com.paditech.cvmarker.model.Personal;
import com.paditech.cvmarker.model.Resume;


/**
 * Created by USER on 12/6/2016.
 */
public class FillOneByOneAdapter extends FragmentPagerAdapter {

    private final static int NUMB_PAGE = 6;

    private Context mContext;
    private Resume mResume;

    private EducationFragment educationFragment;
    private ExperienceFragment experienceFragment;
    private ObjectiveFragment objectiveFragment;
    private OtherSectionFragment otherSectionFragment;
    private PersonalInfoFragment personalInfoFragment;
    private SkillsFragment skillsFragment;

    public FillOneByOneAdapter(FragmentManager fm,Context context, Resume resume) {
        super(fm);
        educationFragment = new EducationFragment();
        experienceFragment = new ExperienceFragment();
        objectiveFragment = new ObjectiveFragment();
        otherSectionFragment = new OtherSectionFragment();
        personalInfoFragment = new PersonalInfoFragment();
        skillsFragment = new SkillsFragment();

        mContext = context;
        mResume = resume;

    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                personalInfoFragment.personal = mResume.getPersonal();
                return personalInfoFragment;
            case 1:
                objectiveFragment.mListObjective = mResume.getObjectiveList();
                return objectiveFragment;
            case 2:
                educationFragment.mListEducation = mResume.getEducationList();
                return educationFragment;
            case 3:
                experienceFragment.mListExp = mResume.getWorkExperienceList();
                return experienceFragment;
            case 4:
                skillsFragment.mListSkill = mResume.getSkillList();
                return skillsFragment;
            case 5:
                otherSectionFragment.mListOther = mResume.getOtherInfoList();
                return otherSectionFragment;
        }
        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return NUMB_PAGE;
    }

    public Resume getResume() {
        return mResume;
    }

    public void savePersonalForm(int index) {
        if(!Personal.personalIsEmpty(personalInfoFragment.personal)) {
            mResume.setPersonal(personalInfoFragment.personal);
        }
        if(objectiveFragment.mListObjective!= null && objectiveFragment.mListObjective.size() > 0) {
            mResume.setObjectiveList(objectiveFragment.mListObjective);
        }
        if(educationFragment.mListEducation != null && educationFragment.mListEducation.size() > 0) {
            mResume.setEducationList(educationFragment.mListEducation);
        }
        if(experienceFragment.mListExp != null && experienceFragment.mListExp.size() > 0)
            mResume.setWorkExperienceList(experienceFragment.mListExp);

        if(skillsFragment.mListSkill != null && skillsFragment.mListSkill.size() > 0)
            mResume.setSkillList(skillsFragment.mListSkill);

        if(otherSectionFragment.mListOther != null && otherSectionFragment.mListOther.size() > 0)
            mResume.setOtherInfoList(otherSectionFragment.mListOther);


    }

    public void onAddClicked(int positon) {
        switch (positon) {
            case 0:
                break;
            case 1:
                break;
        }
    }

    public void onResetClicked(int position) {
        switch (position)
        {
            case 0:
                personalInfoFragment.onReset();
                break;
            case 1:
                objectiveFragment.reset();
                break;
            case 2:
                educationFragment.reset();
                break;
            case 3:
                experienceFragment.reset();
                break;
            case 4: skillsFragment.reset();
                break;
            case 5: otherSectionFragment.reset();
                break;
        }
    }

    public void onDoneClicked(int position) {
        switch (position) {
            case 0:
                personalInfoFragment.saveForm();
                break;
            case 1:
                objectiveFragment.addDone();
                break;
            case 2:
                educationFragment.addDone();
                break;
            case 3:
                experienceFragment.addDone();
                break;
            case 4:
                skillsFragment.addDone();
                break;
            case 5:
                otherSectionFragment.addDone();
                break;
        }
    }

    public boolean doContinue(int position) {
        switch (position) {
            case 0:
                return personalInfoFragment.doContinue();
            case 1:
                return objectiveFragment.doContinue();
            case 2:
                return educationFragment.doContinue();
            case 3:
                return experienceFragment.doContinue();
            case 4:
                return skillsFragment.doContinue();
            case 5:
                return otherSectionFragment.doContinue();
        }
        return false;
    }


}

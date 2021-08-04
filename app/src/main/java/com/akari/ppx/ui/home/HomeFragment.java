package com.akari.ppx.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.akari.ppx.R;
import com.akari.ppx.common.constant.Const;
import com.akari.ppx.common.constant.Prefs;
import com.akari.ppx.common.preference.EditPreference;
import com.akari.ppx.common.preference.EditPreferenceDialogFragCompat;
import com.akari.ppx.common.preference.NeutralEditPreferenceDialogFragCompat;
import com.akari.ppx.common.preference.TestButton;
import com.akari.ppx.common.preference.TestEditPreference;
import com.akari.ppx.common.utils.ModuleUtils;
import com.akari.ppx.common.utils.Utils;
import com.akari.ppx.ui.channel.ChannelActivity;

import java.util.Objects;

public class HomeFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
	private final HomeActivity context;
	private boolean isModuleEnabled;

	public HomeFragment(HomeActivity context) {
		this.context = context;
	}

	@SuppressLint("ApplySharedPref")
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (isModuleEnabled) return;
		ActionBar actionBar = ((HomeActivity) requireActivity()).getSupportActionBar();
		Objects.requireNonNull(actionBar).setTitle(String.format("%s [%s]", actionBar.getTitle(), "未激活"));
	}

	@Override
	public void onPause() {
		super.onPause();
		Utils.setPreferenceWorldWritable(context);
	}

	@Override
	public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
		addPreferencesFromResource(R.xml.settings);
		isModuleEnabled = ModuleUtils.isModuleEnabled_Xposed() || ModuleUtils.isModuleEnabled_Taichi(context);
		findPreference(Prefs.JOIN_QQ_GROUP).setVisible(isModuleEnabled);
		findPreference(Prefs.DIY_CATEGORY_LIST).setOnPreferenceChangeListener(this);
		findPreference(Prefs.AUTO_DIGG_ENABLE).setOnPreferenceChangeListener(this);
		findPreference(Prefs.AUTO_DISS_ENABLE).setOnPreferenceChangeListener(this);
		findPreference(Prefs.REMOVE_BOTTOM_VIEW).setOnPreferenceChangeListener(this);
		findPreference(Prefs.REMOVE_PUBLISH_BUTTON).setOnPreferenceChangeListener(this);
		findPreference(Prefs.HIDE_LAUNCHER_ICON).setOnPreferenceChangeListener(this);
		findPreference(Prefs.DONATE).setOnPreferenceClickListener(this);
		findPreference(Prefs.JOIN_QQ_GROUP).setOnPreferenceClickListener(this);
		findPreference(Prefs.SOURCE_CODE).setOnPreferenceClickListener(this);
		Preference prefVer = findPreference(Prefs.BEST_FIT_VERSION);
		prefVer.setSummary(String.format(prefVer.getSummary().toString(), Const.BEST_FIT_VERSION));
		setSummaryT(Prefs.REMOVE_COMMENT_KEYWORDS, Utils::checkListSize, true);
		setSummaryT(Prefs.REMOVE_COMMENT_USERS, Utils::checkListSize, true);
		setSummaryT(Prefs.REMOVE_ITEM_KEYWORDS, Utils::checkListSize, true);
		setSummaryT(Prefs.REMOVE_ITEM_USERS, Utils::checkListSize, true);
		setSummaryT(Prefs.RECENT_COMMENT_TIME_FORMAT, Utils::checkListSize, true);
		setSummaryT(Prefs.EXACT_COMMENT_TIME_FORMAT, Utils::getNowDate, false);
		setSummary(Prefs.AUTO_BROWSE_FREQUENCY);
		setSummary(Prefs.AUTO_COMMENT_TEXT);
		setSummary(Prefs.USER_NAME);
		setSummary(Prefs.CERTIFY_DESC);
		setSummary(Prefs.DESCRIPTION);
		setSummaryT(Prefs.LIKE_COUNT, Utils::checkIsLongValid, false);
		setSummaryT(Prefs.FOLLOWERS_COUNT, Utils::checkIsLongValid, false);
		setSummaryT(Prefs.FOLLOWING_COUNT, Utils::checkIsLongValid, false);
		setSummaryT(Prefs.POINT, Utils::checkIsLongValid, false);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		switch (Prefs.fromString(preference.getKey())) {
			case DONATE:
				Utils.donateByAlipay(context);
				break;
			case JOIN_QQ_GROUP:
				Utils.joinQQGroup(context);
				break;
			case SOURCE_CODE:
				Utils.showGitPage(context);
				break;
			default:
				return false;
		}
		return true;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		boolean checked = (Boolean) newValue;
		switch (Prefs.fromString(preference.getKey())) {
			case DIY_CATEGORY_LIST:
				if (checked)
					context.startActivity(new Intent(context, ChannelActivity.class));
				return true;
			case AUTO_DIGG_ENABLE: {
				if (checked)
					((SwitchPreference) findPreference(Prefs.AUTO_DISS_ENABLE)).setChecked(false);
				return true;
			}
			case AUTO_DISS_ENABLE: {
				if (checked)
					((SwitchPreference) findPreference(Prefs.AUTO_DIGG_ENABLE)).setChecked(false);
				return true;
			}
			case REMOVE_BOTTOM_VIEW: {
				if (checked)
					((SwitchPreference) findPreference(Prefs.REMOVE_PUBLISH_BUTTON)).setChecked(false);
				return true;
			}
			case REMOVE_PUBLISH_BUTTON: {
				if (checked)
					((SwitchPreference) findPreference(Prefs.REMOVE_BOTTOM_VIEW)).setChecked(false);
				return true;
			}
			case HIDE_LAUNCHER_ICON: {
				Utils.hideIcon(context, checked);
				return true;
			}
		}
		return false;
	}

	@Override
	public void onDisplayPreferenceDialog(Preference preference) {
		boolean handled = false;
		if (preference instanceof EditPreference) {
			String key = preference.getKey();
			DialogFragment dialogFragment;
			if (preference instanceof TestEditPreference) {
				dialogFragment = NeutralEditPreferenceDialogFragCompat.newInstance(key, ((TestEditPreference) preference).getTestButton());
			} else dialogFragment = EditPreferenceDialogFragCompat.newInstance(key);
			dialogFragment.setTargetFragment(this, 0);
			dialogFragment.show(context.getSupportFragmentManager(), "android.support.v7.preference.PreferenceFragment.DIALOG");
			handled = true;
		}
		if (!handled)
			super.onDisplayPreferenceDialog(preference);
	}

	@NonNull
	private Preference findPreference(Prefs prefs) {
		return Objects.requireNonNull(findPreference(prefs.getKey()));
	}

	private void setSummary(Prefs prefs) throws NullPointerException {
		EditPreference pref = (EditPreference) findPreference(prefs);
		String text = pref.getText();
		pref.setSummary(pref instanceof TestEditPreference && ((TestEditPreference) pref).isArray() ? Utils.checkListSize(text) : "".equals(text) ? "" : Const.NOW.concat(text));
	}

	private void setSummaryT(Prefs prefs, TestButton.Listener listener, boolean isArray) {
		EditPreference pref = (EditPreference) findPreference(prefs);
		if (pref instanceof TestEditPreference) {
			((TestEditPreference) pref).setIsArray(isArray);
			((TestEditPreference) pref).setTestButton(new TestButton(listener));
		}
		setSummary(prefs);
	}
}

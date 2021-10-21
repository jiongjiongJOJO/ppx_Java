package com.akari.ppx.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.akari.ppx.BuildConfig;
import com.akari.ppx.R;
import com.akari.ppx.ui.BaseActivity;

import org.jetbrains.annotations.NotNull;

import static com.akari.ppx.common.utils.Utils.jump2user;

public class HomeActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.home_content, new HomeFragment(this))
				.commit();
		setSupportActionBar(findViewById(R.id.toolbar));
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setTitle(String.format("%s %s", getString(R.string.app_name), BuildConfig.VERSION_NAME));
			actionBar.setHomeButtonEnabled(true);
			actionBar.setDisplayHomeAsUpEnabled(false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(@NotNull Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@SuppressLint("NonConstantResourceId")
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case R.id.start_app:
				Intent intent = getPackageManager().getLaunchIntentForPackage("com.sup.android.superb");
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case R.id.jump_to_user:
				jump2user(this);
				break;
			case R.id.exit:
				finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
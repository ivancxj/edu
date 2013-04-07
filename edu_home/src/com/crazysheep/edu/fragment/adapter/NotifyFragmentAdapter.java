package com.crazysheep.edu.fragment.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.crazysheep.edu.fragment.NotifyFragment;

public class NotifyFragmentAdapter extends FragmentPagerAdapter{
	protected static final String[] CONTENT = new String[] {
			"<p>尊敬的各位家长:</p>"
					+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;为了帮助孩子更好地在幼儿园学习与生活，也为了让我们成为因孩子而联系在一起的朋友，我们将于2011年9月27日（周二）下午四点召开各班家长会</p>"
					+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;鉴于本次会议重要，请父母尽量安排好工作 亲自参加，真诚期待您的参与，同时感谢您对幼儿园工作的支持！</p>"
					+ "<p>XXX幼儿园</p> <p>2012-02-01</p>",
			"<p>尊敬的各位家长:</p>"
					+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;为了帮助孩子更好地在幼儿园学习与生活，也为了让我们成为因孩子而联系在一起的朋友，我们将于2011年9月28日（周三）下午四点召开各班家长会</p>"
					+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;鉴于本次会议重要，请父母尽量安排好工作 亲自参加，真诚期待您的参与，同时感谢您对幼儿园工作的支持！</p>"
					+ "<p>XXX幼儿园</p> <p>2012-02-02</p>",
			"<p>尊敬的各位家长:</p>"
					+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;为了帮助孩子更好地在幼儿园学习与生活，也为了让我们成为因孩子而联系在一起的朋友，我们将于2011年9月29日（周四）下午四点召开各班家长会</p>"
					+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;鉴于本次会议重要，请父母尽量安排好工作 亲自参加，真诚期待您的参与，同时感谢您对幼儿园工作的支持！</p>"
					+ "<p>XXX幼儿园</p> <p>2012-02-03</p>",
			"<p>尊敬的各位家长:</p>"
					+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;为了帮助孩子更好地在幼儿园学习与生活，也为了让我们成为因孩子而联系在一起的朋友，我们将于2011年9月30日（周五）下午四点召开各班家长会</p>"
					+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;鉴于本次会议重要，请父母尽量安排好工作 亲自参加，真诚期待您的参与，同时感谢您对幼儿园工作的支持！</p>"
					+ "<p>XXX幼儿园</p> <p>2012-02-04</p>" };
	private int mCount = CONTENT.length;
	
	public NotifyFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	/*
	 * @Override public int getIconResId(int index) { // TODO Auto-generated
	 * method stub return 0; }
	 */

	@Override
	public Fragment getItem(int position) {
		return NotifyFragment.newInstance(CONTENT[position % CONTENT.length]);
	}

	@Override
	public int getCount() {
		return mCount;
	}

}

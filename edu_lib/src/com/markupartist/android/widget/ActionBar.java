/*
 * Copyright (C) 2010 Johan Nilsson <http://markupartist.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.markupartist.android.widget;

import java.util.LinkedList;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.lib.R;

public class ActionBar extends RelativeLayout implements OnClickListener, ViewPager.OnPageChangeListener {

    private LayoutInflater mInflater;
    private RelativeLayout mBarView;
    private ImageView mLogoView;
    private View mBackIndicator;
    // private View mHomeView;
    private TextView mTitleView;
    private LinearLayout mActionsView;
    private LinearLayout mActionMenusView;
    private LinearLayout mActionMenusWrapper;
    private ImageButton mHomeBtn;
    private LinearLayout mHomeLayout;
    private ProgressBar mProgress;
    private ImageView mMenuMark;
    private ChildMethod mChildMethod;

    public interface ChildMethod {
        void doChildMetod(int index);
    }

    public void setChildMethod(ChildMethod childMethod) {
        mChildMethod = childMethod;
    }

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mBarView = (RelativeLayout) mInflater.inflate(R.layout.actionbar, null);
        addView(mBarView);

        mLogoView = (ImageView) mBarView.findViewById(R.id.actionbar_home_logo);
        mHomeLayout = (LinearLayout) mBarView
                .findViewById(R.id.actionbar_home_bg);
        mHomeBtn = (ImageButton) mBarView.findViewById(R.id.actionbar_home_btn);
        mBackIndicator = mBarView.findViewById(R.id.actionbar_home_is_back);

        mTitleView = (TextView) mBarView.findViewById(R.id.actionbar_title);
        mActionsView = (LinearLayout) mBarView
                .findViewById(R.id.actionbar_actions);
        mActionMenusView = (LinearLayout) mBarView
                .findViewById(R.id.actionbar_menus);
        mActionMenusWrapper = (LinearLayout) mBarView
                .findViewById(R.id.actionbar_menus_warpper);


        mMenuMark = (ImageView) mBarView.findViewById(R.id.menu_mark);

        mProgress = (ProgressBar) mBarView
                .findViewById(R.id.actionbar_progress);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ActionBar);
        CharSequence title = a.getString(R.styleable.ActionBar_title);
        if (title != null) {
            setTitle(title);
        }
        a.recycle();
    }

    /**
     * set home action
     *
     * @param action
     */
    public void setHomeAction(Action action) {
        mHomeBtn.setOnClickListener(this);
        mHomeBtn.setTag(action);
        if (action.getDrawable() != null) {
            mHomeBtn.setImageResource(action.getDrawable());
            mHomeBtn.setVisibility(View.VISIBLE);
        }
        mHomeLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Shows the provided logo to the left in the action bar.
     * <p/>
     * This is ment to be used instead of the setHomeAction and does not draw a
     * divider to the left of the provided logo.
     *
     * @param resId The drawable resource id
     */
    public void setHomeLogo(int resId) {
        // TODO: Add possibility to add an IntentAction as well.
        mLogoView.setImageResource(resId);
        mLogoView.setVisibility(View.VISIBLE);
        mHomeLayout.setVisibility(View.GONE);
    }

    /*
     * Emulating Honeycomb, setdisplayHomeAsUpEnabled takes a boolean and
     * toggles whether the "home" view should have a little triangle indicating
     * "up"
     */
    public void setDisplayHomeAsUpEnabled(boolean show) {
        mBackIndicator.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void setTitle(int resid) {
        mTitleView.setText(resid);
    }

    public void setTitle(CharSequence title) {
        mTitleView.setText(title);
    }

    public void setLeftTitle(String title) {
        mTitleView.setText(title);
        int width = getResources().getDimensionPixelSize(
                R.dimen.actionbar_item_width);
        LayoutParams params = (LayoutParams) mTitleView.getLayoutParams();
        params.leftMargin = width;
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 1);
        mTitleView.setLayoutParams(params);
    }

    public void setTitleAlignToLeft(int textSize) {

        mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        LayoutParams params = (LayoutParams) mTitleView.getLayoutParams();
        int width = getResources().getDimensionPixelSize(
                R.dimen.actionbar_item_width)
                + IgnitedScreens.dipToPx(getContext(), 9);
        params.leftMargin = width;
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 1);
        mTitleView.setLayoutParams(params);
    }

    /**
     * Set the enabled state of the progress bar.
     *
     * @param visibility of {@link android.view.View#VISIBLE}, {@link android.view.View#INVISIBLE}, or
     *                   {@link android.view.View#GONE}.
     */
    public void setProgressBarVisibility(int visibility) {
        mProgress.setVisibility(visibility);
        int childCount = mActionsView.getChildCount();
        View v = mActionsView.getChildAt(childCount - 1);
        if (v != null) {
            View btn = v.findViewById(R.id.actionbar_item_btn);
            View text = v.findViewById(R.id.actionbar_item_text);
            if (View.VISIBLE == visibility) {
                if (btn.getVisibility() == View.VISIBLE)
                    btn.setVisibility(View.INVISIBLE);
                if (text.getVisibility() == View.VISIBLE)
                    text.setVisibility(View.INVISIBLE);
            } else if (View.INVISIBLE == visibility) {
                if (btn.getVisibility() == View.INVISIBLE)
                    btn.setVisibility(View.VISIBLE);
                if (text.getVisibility() == View.INVISIBLE)
                    text.setVisibility(View.VISIBLE);
            }

        }
    }

    /**
     * Returns the visibility status for the progress bar.
     * <p/>
     * <p/>
     * return  of {@link android.view.View#VISIBLE}, {@link android.view.View#INVISIBLE}, or
     * {@link android.view.View#GONE}.
     */
    public int getProgressBarVisibility() {
        return mProgress.getVisibility();
    }

    /**
     * Function to set a click listener for Title TextView
     *
     * @param listener the onClickListener
     */
    public void setOnTitleClickListener(OnClickListener listener) {
        mTitleView.setOnClickListener(listener);
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int index) {
        View v = mActionMenusView.getChildAt(index);
        mLocation = mMenuMark.getWidth() + mPos;
        startAnimation(mStartLeft, mLocation * index, 0, 0);
        mStartLeft = mLocation * index;
        if (mChildMethod != null) {
            mChildMethod.doChildMetod(index);
        }
    }

    @Override
    public void onPageScrollStateChanged(int index) {
    }

    @Override
    public void onClick(View view) {
        final Object tag = view.getTag();
        if (tag instanceof Action) {
            Action action = (Action) tag;
            action.performAction(view);
            if (tag instanceof PagerAction)
                onMenuClick(((PagerAction) tag));
        }
    }

    private int mStartLeft;
    private int mLocation;
    private int mPos = 4;

    public void onMenuClick(PagerAction pager) {
        pager.getViewPager().setCurrentItem(pager.getPosition(), true);
        mLocation = mMenuMark.getLayoutParams().width + IgnitedScreens.dipToPx(getContext(), mPos);
        startAnimation(mStartLeft, mLocation * pager.getPosition(), 0, 0);
        mStartLeft = mLocation * pager.getPosition();
    }

    public void onMenuClick(int pager) {
        int count = mActionMenusView.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = mActionMenusView.getChildAt(count - i - 1);
            PagerAction action = (PagerAction) view.getTag();
            if (action != null) {
                action.getViewPager().setCurrentItem(action.getPosition(), true);
                mLocation = mMenuMark.getLayoutParams().width + IgnitedScreens.dipToPx(getContext(), mPos);
                startAnimation(mStartLeft, mLocation * action.getPosition(), 0, 0);
                mStartLeft = mLocation * action.getPosition();
            }
        }
    }

    public void startAnimation(int startX, int toX, int startY, int toY) {
        TranslateAnimation animation = new TranslateAnimation(startX, toX, startY, toY);
        animation.setDuration(300);
        animation.setFillAfter(true);
        mMenuMark.startAnimation(animation);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * Adds a list of {@link Action}s.
     *
     * @param actionList the actions to add
     */
    public void addActions(ActionList actionList) {
        int actions = actionList.size();
        for (int i = 0; i < actions; i++) {
            addAction(actionList.get(i));
        }
    }

    /**
     * Adds a new {@link Action}.
     *
     * @param action the action to add
     */
    public void addAction(Action action) {
        final int index = mActionsView.getChildCount();
        addAction(action, index);
    }

    /**
     * Adds a new {@link Action}.
     *
     * @param action the action to add
     */
    public void addActionMenu(Action action) {
        final int index = mActionMenusView.getChildCount();
        mActionMenusView.addView(inflateActionMenu(action), index);
        mActionMenusWrapper.setVisibility(View.VISIBLE);
        if (action instanceof PagerAction) {
            ((PagerAction) action).setPosition(index);
            ((PagerAction) action).getViewPager().setOnPageChangeListener(this);
        }
    }

    /**
     * Adds a new {@link Action} at the specified index.
     *
     * @param action the action to add
     * @param index  the position at which to add the action
     */
    public void addAction(Action action, int index) {
        mActionsView.addView(inflateAction(action), index);
    }

    /**
     * Removes all action views from this action bar
     */
    public void removeAllActions() {
        mActionsView.removeAllViews();
    }

    /**
     * Remove a action from the action bar.
     *
     * @param index position of action to remove
     */
    public void removeActionAt(int index) {
        if (mActionsView.getChildCount() > index)
            mActionsView.removeViewAt(index);
    }

    /**
     * Remove a action from the action bar.
     *
     * @param action The action to remove
     */
    public void removeAction(Action action) {
        int childCount = mActionsView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = mActionsView.getChildAt(i);
            if (view != null) {
                final Object tag = view.getTag();
                if (tag instanceof Action && tag.equals(action)) {
                    mActionsView.removeView(view);
                }
            }
        }
    }


    /**
     * Returns the number of actions currently registered with the action bar.
     *
     * @return action count
     */
    public int getActionCount() {
        return mActionsView.getChildCount();
    }

    /**
     * Inflates a {@link android.view.View} with the given {@link Action}.
     *
     * @param action the action to inflate
     * @return a view
     */
    private View inflateActionMenu(Action action) {
        Button menu = (Button) mInflater.inflate(R.layout.actionbar_menuitem, mActionsView,
                false);
        if (action.getDrawable() != null) {
            menu.setBackgroundResource(action.getDrawable());
        }
        if (action.getTextValue() != null) {
            menu.setText(action.getTextValue());
        }
        menu.setTag(action);
        menu.setOnClickListener(this);
        return menu;
    }

    /**
     * Inflates a {@link android.view.View} with the given {@link Action}.
     *
     * @param action the action to inflate
     * @return a view
     */
    private View inflateAction(Action action) {
        View view = mInflater.inflate(R.layout.actionbar_item, mActionsView,
                false);
        ImageButton labelView = (ImageButton) view.findViewById(R.id.actionbar_item_btn);
        TextView textView = (TextView) view.findViewById(R.id.actionbar_item_text);
        if (action.getDrawable() != null) {
            labelView.setImageResource(action.getDrawable());
            labelView.setVisibility(View.VISIBLE);
            labelView.setTag(action);
            labelView.setOnClickListener(this);
        } else if (action.getTextValue() != null) {
            textView.setText(action.getTextValue());
            textView.setVisibility(View.VISIBLE);
            textView.setTag(action);
            textView.setOnClickListener(this);
        }
        return view;
    }

    public void notifyActionUI() {
        int count = mActionsView.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = mActionsView.getChildAt(i);
            ImageButton labelView = (ImageButton) view.findViewById(R.id.actionbar_item_btn);
            TextView textView = (TextView) view.findViewById(R.id.actionbar_item_text);
            if (labelView.getTag() != null) {
                Action action = (Action) labelView.getTag();
                labelView.setImageResource(action.getDrawable());
            }
            if (textView.getTag() != null) {
                Action action = (Action) textView.getTag();
                textView.setText(action.getTextValue());
            }
        }
    }

    /**
     * A {@link java.util.LinkedList} that holds a list of {@link Action}s.
     */
    public static class ActionList extends LinkedList<Action> {

        private static final long serialVersionUID = -7639253919045641775L;
    }

    /**
     * Definition of an action that could be performed, along with a icon to
     * show.
     */
    public interface Action {

        public Integer getDrawable();

        public String getTextValue();

        public void performAction(View view);

        public void setTextValue(String value);

    }

    public static abstract class AbstractAction implements Action {

        private Integer mDrawable;
        private String mText;

        protected AbstractAction(Integer pDrawable, String pText) {
            this.mDrawable = pDrawable;
            this.mText = pText;
        }

        @Override
        public Integer getDrawable() {
            return mDrawable;
        }

        @Override
        public String getTextValue() {
            return mText;
        }

        @Override
        public void setTextValue(String value) {
            mText = value;
        }

    }

    public static class PagerAction extends AbstractAction {

        private ViewPager mPager;
        private int mPosition;

        public PagerAction(String pText, ViewPager pPager) {
            super(null, pText);
            this.mPager = pPager;
        }

        public ViewPager getViewPager() {
            return this.mPager;
        }

        public int getPosition() {
            return mPosition;
        }

        public void setPosition(int mPosition) {
            this.mPosition = mPosition;
        }

        @Override
        public void performAction(View view) {
            getViewPager().setCurrentItem(getPosition());
        }

    }

    public static class IntentAction extends AbstractAction {

        private Context mContext;
        private Intent mIntent;
        protected String mTag;

        public IntentAction(Context context) {
            super(null, null);
            mContext = context;
        }

        public IntentAction(Context context, int drawableId, String tag) {
            super(drawableId, null);
            mTag = tag;
            mContext = context;
        }

        public IntentAction(Context context, String text, String tag) {
            super(null, text);
            mTag = tag;
            mContext = context;
        }

        public IntentAction(Context context, Intent intent, String text, String tag) {
            super(null, text);
            mIntent = intent;
            mTag = tag;
            mContext = context;
        }

        @Deprecated
        public IntentAction(Context context, Integer drawableId, Integer textId, String tag) {
            super(drawableId, textId != null ? context.getString(textId) : null);
            mTag = tag;
            mContext = context;
        }

        public IntentAction(Context context, Intent intent, Integer drawableId) {
            super(drawableId, null);
            mContext = context;
            mIntent = intent;
        }

        public String getTag() {
            return mTag;
        }

        @Override
        public void performAction(View view) {
            try {
                if (mIntent != null)
                    mContext.startActivity(mIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(
                        mContext,
                        mContext.getText(R.string.actionbar_activity_not_found),
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

	/*
     * public static abstract class SearchAction extends AbstractAction { public
	 * SearchAction() { super(R.drawable.actionbar_search); } }
	 */
}

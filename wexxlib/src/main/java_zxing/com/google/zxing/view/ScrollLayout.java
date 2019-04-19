package com.google.zxing.view;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xw.wexxlib.R;


/**
 * Created by Ricky on 2017/3/20.
 */

public class ScrollLayout extends LinearLayout implements View.OnClickListener {

    public static final int INDEX_SECOND = 2;
    public static final int INDEX_THIRD = 3;

    private Context mContext;
    private TextView mSecondArea, mThirdArea;
    private Point mDownPoint;
    private Point mMovePoint;
    private Point mUpPoint;
    private int mTranslationToLeftX;
    private IOnSelectChanged mOnSelectChangedListener;
    private boolean isMoveEvent;

    public ScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.scroll_layout, this, true);
        mContext = context;
        initView();
        initMemberEntity();
    }

    private void initView() {
        mSecondArea = (TextView) findViewById(R.id.second);
        mThirdArea = (TextView) findViewById(R.id.third);
        mSecondArea.setOnClickListener(this);
        mThirdArea.setOnClickListener(this);
        SpannableString ss = new SpannableString(mSecondArea.getText().toString());
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mSecondArea.setText(ss);
    }

    private void initMemberEntity() {
        mDownPoint = new Point();
        mMovePoint = new Point();
        mUpPoint = new Point();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r + getMeasuredWidth() / 3, b);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mDownPoint.x = (int) ev.getX();
            mDownPoint.y = (int) ev.getY();
            isMoveEvent = false;
            return false;
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            mMovePoint.x = (int) ev.getX();
            mMovePoint.y = (int) ev.getY();
            if (Math.abs(mMovePoint.x - mDownPoint.x) >= ViewConfiguration.get(mContext).getScaledTouchSlop()) {
                isMoveEvent = true;
                return true;
            }
            return false;
        }
        if (ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_UP) {
            mUpPoint.x = (int) ev.getX();
            mUpPoint.y = (int) ev.getY();
            if (Math.abs(mUpPoint.x - mDownPoint.x) >= ViewConfiguration.get(mContext).getScaledTouchSlop() || isMoveEvent) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        do {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mDownPoint.x = (int) event.getX();
                mDownPoint.y = (int) event.getY();
                return false;
            }

            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                mMovePoint.x = (int) event.getX();
                mMovePoint.y = (int) event.getY();
                int translationX = mMovePoint.x - mDownPoint.x;
                if (Math.abs(translationX) > ViewConfiguration.get(mContext).getScaledTouchSlop()
                        && Math.abs(translationX) < getWidth() / 3) {
                    if (translationX < 0) {
                        //左移
                        if (Math.abs(mTranslationToLeftX) < getWidth() / 3) {
                            this.scrollTo(-translationX, 0);
                            mTranslationToLeftX = Math.abs(translationX);
                        }
                    } else {
                        //右移
                        if (mTranslationToLeftX - translationX > 0) {
                            this.scrollTo(mTranslationToLeftX - translationX, 0);
                            break;
                        }
                    }
                }
                break;
            }

            if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                mUpPoint.x = (int) event.getX();
                mUpPoint.y = (int) event.getY();
                int slopX = mUpPoint.x - mDownPoint.x;
                if (slopX < 0 && Math.abs(mTranslationToLeftX) < getWidth() / 3) {
                    if (slopX >= -getWidth() / 6) {
                        this.scrollTo(mTranslationToLeftX = 0, 0);
                        SwitchTextSpan(mSecondArea, mThirdArea);
                        mOnSelectChangedListener.selectChanged(INDEX_SECOND);
                    } else {
                        this.scrollTo(mTranslationToLeftX = getWidth() / 3, 0);
                        SwitchTextSpan(mThirdArea, mSecondArea);
                        mOnSelectChangedListener.selectChanged(INDEX_THIRD);
                    }
                } else {
                    if (slopX <= getWidth() / 6 && Math.abs(mTranslationToLeftX) > 0) {
                        this.scrollTo(mTranslationToLeftX = getWidth() / 3, 0);
                        SwitchTextSpan(mThirdArea, mSecondArea);
                        mOnSelectChangedListener.selectChanged(INDEX_THIRD);
                    } else {
                        this.scrollTo(mTranslationToLeftX = 0, 0);
                        SwitchTextSpan(mSecondArea, mThirdArea);
                        mOnSelectChangedListener.selectChanged(INDEX_SECOND);
                    }
                }
                break;
            }
        } while (false);
        return true;
    }

    @Override
    public void onClick(View view) {

        int viewID = view.getId();

        do {

            if (viewID == R.id.second) {
                this.scrollTo(mTranslationToLeftX = 0, 0);
                SwitchTextSpan(mSecondArea, mThirdArea);
                mOnSelectChangedListener.selectChanged(INDEX_SECOND);
                break;
            }

            if (viewID == R.id.third) {
                this.scrollTo(mTranslationToLeftX = getWidth() / 3, 0);
                SwitchTextSpan(mThirdArea, mSecondArea);
                mOnSelectChangedListener.selectChanged(INDEX_THIRD);
                break;
            }

        } while (false);
    }

    private void SwitchTextSpan(TextView bold, TextView normal) {
        SpannableString ss = new SpannableString(bold.getText().toString());
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        bold.setText(ss);
        normal.setText(normal.getText().toString());
    }

    public void setOnSelectChangedListener(IOnSelectChanged onSelectChangedListener) {
        mOnSelectChangedListener = onSelectChangedListener;
    }

    public interface IOnSelectChanged {
        void selectChanged(int index);
    }
}

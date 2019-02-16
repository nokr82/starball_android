package com.devstories.starball_android.swipestack;

import android.animation.Animator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

public class SwipeHelper implements View.OnTouchListener {

    private final SwipeStack mSwipeStack;
    private View mObservedView;

    private boolean mListenForTouchEvents;
    private float mDownX;
    private float mDownY;
    private float mInitialX;
    private float mInitialY;
    private int mPointerId;

    private float mRotateDegrees = SwipeStack.DEFAULT_SWIPE_ROTATION;
    private float mOpacityEnd = SwipeStack.DEFAULT_SWIPE_OPACITY;
    private int mAnimationDuration = SwipeStack.DEFAULT_ANIMATION_DURATION;

    private boolean floating = false;

    public boolean isFloating() {
        return floating;
    }

    public void setFloating(boolean floating) {
        this.floating = floating;
    }

    public SwipeHelper(SwipeStack swipeStack) {
        mSwipeStack = swipeStack;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        System.out.println(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mListenForTouchEvents || !mSwipeStack.isEnabled()) {
                    return false;
                }

                v.getParent().requestDisallowInterceptTouchEvent(true);
                mSwipeStack.onSwipeStart();
                mPointerId = event.getPointerId(0);
                mDownX = event.getX(mPointerId);
                mDownY = event.getY(mPointerId);

                return true;

            case MotionEvent.ACTION_MOVE:
                int pointerIndex = event.findPointerIndex(mPointerId);
                if (pointerIndex < 0) return false;

                floating = true;

                float dx = event.getX(pointerIndex) - mDownX;
                float dy = event.getY(pointerIndex) - mDownY;

                mObservedView.setAnimation(null);

                float newX = mObservedView.getX() + dx;
                float newY = mObservedView.getY() + dy;

                System.out.println("newX : " + newX + ", dy : " + dy + ", newY : " + newY);

                mObservedView.setX(0);
                // mObservedView.setX(mObservedView.getX());
                // mObservedView.setX(newX);
                // mObservedView.setY((int) newY);

                mObservedView.setY((int) newY);

                System.out.println("mObservedView.getY() 0 : " + mObservedView.getY() + ", mObservedView.hs : " + mObservedView.hashCode());

                // float dragDistanceX = newX - mInitialX;
                // float swipeProgress = Math.min(Math.max(dragDistanceX / mSwipeStack.getWidth(), -1), 1);
                float dragDistanceY = newY - mInitialY;
                float swipeProgress = Math.min(Math.max(dragDistanceY / mSwipeStack.getHeight(), -1), 1);

                mSwipeStack.onSwipeProgress(swipeProgress);

                System.out.println("mObservedView.getY() 0-1 : " + mObservedView.getY() + ", mObservedView.hs : " + mObservedView.hashCode());

                if (mRotateDegrees > 0) {
                    float rotation = mRotateDegrees * swipeProgress;
                    mObservedView.setRotation(rotation);
                }

                System.out.println("mObservedView.getY() 0-2 : " + mObservedView.getY() + ", mObservedView.hs : " + mObservedView.hashCode());

                if (mOpacityEnd < 1f) {
                    float alpha = 1 - Math.min(Math.abs(swipeProgress), 1);
                    mObservedView.setAlpha(alpha);
                }

                System.out.println("mObservedView.getY() 0-3 : " + mObservedView.getY() + ", mObservedView.hs : " + mObservedView.hashCode());

                return false;

            case MotionEvent.ACTION_UP:

                System.out.println("mObservedView.getY() 1 : " + mObservedView.getY() + ", mObservedView.hs : " + mObservedView.hashCode());

                floating = false;
                v.getParent().requestDisallowInterceptTouchEvent(false);

                System.out.println("mObservedView.getY() 2 : " + mObservedView.getY() + ", mObservedView.hs : " + mObservedView.hashCode());

                mSwipeStack.onSwipeEnd();

                System.out.println("mObservedView.getY() 3 : " + mObservedView.getY() + ", mObservedView.hs : " + mObservedView.hashCode());

                checkViewPosition();

                return true;

            case MotionEvent.ACTION_CANCEL:
                floating = false;
                v.getParent().requestDisallowInterceptTouchEvent(false);
                mSwipeStack.onSwipeEnd();
                checkViewPosition();

                return true;

        }

        return false;
    }

    private void checkViewPosition() {

        System.out.println("checkViewPosition : " + mSwipeStack.isEnabled());

        if (!mSwipeStack.isEnabled()) {
            resetViewPosition();
            return;
        }

        System.out.println("mObservedView.getY() 4 : " + mObservedView.getY() + ", mObservedView.hs : " + mObservedView.hashCode());

        float viewCenterVertical = mObservedView.getY() + (mObservedView.getHeight() / 2);
        float divider = mSwipeStack.getHeight() / 20f;
        float topThreshhold = divider * 9;
        float bottomThreshhold = divider * 11;

        if (viewCenterVertical < topThreshhold) {
            swipeViewToTop(mAnimationDuration);
        } else if (viewCenterVertical > bottomThreshhold) {
            swipeViewToBottom(mAnimationDuration);
        } else {
            resetViewPosition();
        }
    }

    private void checkViewPositionOld() {
        if (!mSwipeStack.isEnabled()) {
            resetViewPosition();
            return;
        }

        float viewCenterHorizontal = mObservedView.getX() + (mObservedView.getWidth() / 2);
        float parentFirstThird = mSwipeStack.getWidth() / 3f;
        float parentLastThird = parentFirstThird * 2;

        if (viewCenterHorizontal < parentFirstThird &&
                mSwipeStack.getAllowedSwipeDirections() != SwipeStack.SWIPE_DIRECTION_ONLY_RIGHT) {
            swipeViewToLeft(mAnimationDuration / 2);
        } else if (viewCenterHorizontal > parentLastThird &&
                mSwipeStack.getAllowedSwipeDirections() != SwipeStack.SWIPE_DIRECTION_ONLY_LEFT) {
            swipeViewToRight(mAnimationDuration / 2);
        } else {
            resetViewPosition();
        }
    }

    private void resetViewPosition() {

        System.out.println("mObservedView.getY() 5 : " + mObservedView.getY() + ", mObservedView.hs : " + mObservedView.hashCode());

        mObservedView.animate()
                .x(mInitialX)
                .y(mInitialY)
                .rotation(0)
                .alpha(1)
                .setDuration(mAnimationDuration)
                .setInterpolator(new OvershootInterpolator(2f))
                .setListener(null);
    }

    private void swipeViewToTop(int duration) {
        if (!mListenForTouchEvents) return;
        mListenForTouchEvents = false;
        mObservedView.animate().cancel();
        mObservedView.animate()
                .y(-mSwipeStack.getHeight() + mObservedView.getY())
                .rotation(-mRotateDegrees)
                .alpha(0.5f)
                .setDuration(duration)
                .setListener(new AnimationUtils.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mSwipeStack.onViewSwipedToTop();
                    }
                });
    }

    private void swipeViewToBottom(int duration) {
        if (!mListenForTouchEvents) return;
        mListenForTouchEvents = false;
        mObservedView.animate().cancel();
        mObservedView.animate()
                .y(mSwipeStack.getHeight() + mObservedView.getY())
                .rotation(mRotateDegrees)
                .alpha(0.5f)
                .setDuration(duration)
                .setListener(new AnimationUtils.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mSwipeStack.onViewSwipedToBottom();
                    }
                });
    }

    private void swipeViewToLeft(int duration) {
        if (!mListenForTouchEvents) return;
        mListenForTouchEvents = false;
        mObservedView.animate().cancel();
        mObservedView.animate()
                .x(-mSwipeStack.getWidth() + mObservedView.getX())
                .rotation(-mRotateDegrees)
                .alpha(0f)
                .setDuration(duration)
                .setListener(new AnimationUtils.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mSwipeStack.onViewSwipedToLeft();
                    }
                });
    }

    private void swipeViewToRight(int duration) {
        if (!mListenForTouchEvents) return;
        mListenForTouchEvents = false;
        mObservedView.animate().cancel();
        mObservedView.animate()
                .x(mSwipeStack.getWidth() + mObservedView.getX())
                .rotation(mRotateDegrees)
                .alpha(0f)
                .setDuration(duration)
                .setListener(new AnimationUtils.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mSwipeStack.onViewSwipedToRight();
                    }
                });
    }

    public void registerObservedView(View view, float initialX, float initialY) {
        if (view == null) return;
        mObservedView = view;
        mObservedView.setOnTouchListener(this);
        mInitialX = initialX;
        mInitialY = initialY;
        mListenForTouchEvents = true;
    }

    public void unregisterObservedView() {
        if (mObservedView != null) {
            mObservedView.setOnTouchListener(null);
        }
        mObservedView = null;
        mListenForTouchEvents = false;
    }

    public void setAnimationDuration(int duration) {
        mAnimationDuration = duration;
    }

    public void setRotation(float rotation) {
        mRotateDegrees = rotation;
    }

    public void setOpacityEnd(float alpha) {
        mOpacityEnd = alpha;
    }

    public void swipeViewToLeft() {
        swipeViewToLeft(mAnimationDuration);
    }

    public void swipeViewToRight() {
        swipeViewToRight(mAnimationDuration);
    }

}

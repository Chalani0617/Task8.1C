package deakinsit305.chalani.a81c_sit305;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

public class MessageItemAnimator extends DefaultItemAnimator {

    @Override
    public boolean animateAdd(@NonNull RecyclerView.ViewHolder holder) {
        final View view = holder.itemView;
        view.setAlpha(0f);
        view.animate()
            .alpha(1f)
            .setDuration(300)
            .setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    dispatchAddFinished(holder);
                }
            })
            .start();
        return true;
    }

    @Override
    public void runPendingAnimations() {
        super.runPendingAnimations();
    }

    @Override
    public void endAnimation(@NonNull RecyclerView.ViewHolder item) {
        item.itemView.animate().cancel();
        item.itemView.setAlpha(1f);
        super.endAnimation(item);
    }

    @Override
    public void endAnimations() {
        super.endAnimations();
    }

    @Override
    public boolean isRunning() {
        return super.isRunning();
    }
}

package studio.goldenapp.quiz_ocean;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class FirebaseRepository {

    private OnFirestoreTaskComplete onFirestoreTaskComplete;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    // TODO: Change collection path here ...
    private Query quizRef = firebaseFirestore.collection(QuizListHolder.QUIZ_GRID_ID).whereEqualTo("visibility", "public");

    public FirebaseRepository(OnFirestoreTaskComplete onFirestoreTaskComplete) {
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }

    public void getQuizData() {
        quizRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                onFirestoreTaskComplete.quizListDataAdded(task.getResult().toObjects(QuizListModel.class));
            } else {
                onFirestoreTaskComplete.onError(task.getException());
            }
        });
    }

    public interface OnFirestoreTaskComplete {
        void quizListDataAdded(List<QuizListModel> quizListModelsList);
        void onError(Exception e);
    }

}

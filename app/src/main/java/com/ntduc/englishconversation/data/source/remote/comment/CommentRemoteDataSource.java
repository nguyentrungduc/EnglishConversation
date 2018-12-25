package com.ntduc.englishconversation.data.source.remote.comment;

import android.support.annotation.NonNull;

import com.ntduc.englishconversation.data.model.Comment;
import com.ntduc.englishconversation.data.source.remote.BaseFirebaseDataBase;
import com.ntduc.englishconversation.utils.Constant;
import com.ntduc.englishconversation.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import static com.ntduc.englishconversation.data.model.Status.ADD;
import static com.ntduc.englishconversation.data.model.Status.DELETE;
import static com.ntduc.englishconversation.data.model.Status.EDIT;



public class CommentRemoteDataSource extends BaseFirebaseDataBase implements CommentDataSource {
    private static final int NUM_OF_COMMENT_PER_PAGE = 15;
    private ChildEventListener mUpdateComment;

    public CommentRemoteDataSource(String timelineId) {
        super(Constant.DatabaseTree.COMMENT);
        mReference = mReference.child(timelineId);
    }

    @Override
    public Observable<Comment> createNewComment(final Comment comment) {
        return Observable.create(new ObservableOnSubscribe<Comment>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Comment> emitter)
                    throws Exception {
                mReference.push()
                        .setValue(comment)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                emitter.onNext(comment);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                emitter.onError(e);
                            }
                        });
            }
        });
    }

    @Override
    public Observable<List<Comment>> getComment(final Comment lastComment) {
        return Observable.create(new ObservableOnSubscribe<List<Comment>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<Comment>> e) throws Exception {
                final Query query;
                if (lastComment == null) {
                    query = mReference.orderByChild(Constant.DatabaseTree.CREATED_AT)
                            .limitToFirst(NUM_OF_COMMENT_PER_PAGE);
                } else {
                    query = mReference.orderByChild(Constant.DatabaseTree.CREATED_AT)
                            .startAt(-lastComment.getCreatedAt(), lastComment.getId())
                            .limitToFirst(NUM_OF_COMMENT_PER_PAGE);
                }
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Comment> comments = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Comment comment = snapshot.getValue(Comment.class);
                            comment.setCreatedAt(
                                    Utils.generateOppositeNumber(comment.getCreatedAt()));
                            comment.setId(snapshot.getKey());
                            if (lastComment == null || !lastComment.getId()
                                    .equals(snapshot.getKey())) {
                                comments.add(comment);
                            }
                        }
                        e.onNext(comments);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        e.onError(databaseError.toException());
                    }
                });
            }
        });
    }

    public Observable<HashMap<Integer, Comment>> registerModifyTimelines(
            final Comment lastComment) {
        return Observable.create(new ObservableOnSubscribe<HashMap<Integer, Comment>>() {
            @Override
            public void subscribe(final ObservableEmitter<HashMap<Integer, Comment>> e)
                    throws Exception {
                final Query query = mReference.orderByChild(Constant.DatabaseTree.CREATED_AT)
                        .endAt(lastComment != null ? -lastComment.getCreatedAt()
                                : -Calendar.getInstance().getTimeInMillis());
                mUpdateComment = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Comment comment = dataSnapshot.getValue(Comment.class);
                        comment.setCreatedAt(
                                Utils.generateOppositeNumber(comment.getCreatedAt()));
                        comment.setId(dataSnapshot.getKey());
                        HashMap<Integer, Comment> commentHashMap = new HashMap<>();
                        commentHashMap.put(ADD, comment);
                        e.onNext(commentHashMap);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Comment comment = dataSnapshot.getValue(Comment.class);
                        comment.setCreatedAt(Utils.generateOppositeNumber(comment.getCreatedAt()));
                        comment.setModifiedAt(
                                Utils.generateOppositeNumber(comment.getModifiedAt()));
                        comment.setId(dataSnapshot.getKey());
                        HashMap<Integer, Comment> commentHashMap = new HashMap<>();
                        commentHashMap.put(EDIT, comment);
                        e.onNext(commentHashMap);
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Comment comment = dataSnapshot.getValue(Comment.class);
                        comment.setCreatedAt(Utils.generateOppositeNumber(comment.getCreatedAt()));
                        comment.setId(dataSnapshot.getKey());
                        HashMap<Integer, Comment> commentHashMap = new HashMap<>();
                        commentHashMap.put(DELETE, comment);
                        e.onNext(commentHashMap);
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        e.onError(databaseError.toException());
                    }
                };
                query.addChildEventListener(mUpdateComment);
            }
        });
    }

    @Override
    public Observable<Comment> updateComment(final Comment comment) {
        return Observable.create(new ObservableOnSubscribe<Comment>() {
            @Override
            public void subscribe(final ObservableEmitter<Comment> emitter) throws Exception {
                mReference.child(comment.getId()).setValue(comment)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                emitter.onNext(comment);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                emitter.onError(e);
                            }
                        });
            }
        });

    }

    @Override
    public Observable<Comment> saveRevisionComment(final Comment comment) {
        return Observable.create(new ObservableOnSubscribe<Comment>() {
            @Override
            public void subscribe(ObservableEmitter<Comment> e) throws Exception {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                        .getReference(Constant.DatabaseTree.COMMENT_REVISION)
                        .child(comment.getId());
                databaseReference.push()
                        .setValue(comment);
            }
        });
    }

    @Override
    public Observable<Comment> deleteComment(final Comment comment) {
        return Observable.create(new ObservableOnSubscribe<Comment>() {
            @Override
            public void subscribe(final ObservableEmitter<Comment> emitter)
                    throws Exception {
                mReference.child(comment.getId())
                        .removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                emitter.onNext(comment);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                emitter.onError(e);
                            }
                        });
            }
        });
    }

    @Override
    public Observable<Comment> saveCommentDelete(final Comment comment) {
        return Observable.create(new ObservableOnSubscribe<Comment>() {
            @Override
            public void subscribe(ObservableEmitter<Comment> e) throws Exception {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                        .getReference(Constant.DatabaseTree.COMMENT_DELETE)
                        .child(comment.getPostId());
                databaseReference.push()
                        .setValue(comment);
            }
        });
    }

    @Override
    public void removeListener() {
        if (mUpdateComment == null) {
            return;
        }
        mReference.removeEventListener(mUpdateComment);
    }
}

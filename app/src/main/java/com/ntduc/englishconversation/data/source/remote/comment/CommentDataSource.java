package com.ntduc.englishconversation.data.source.remote.comment;

import com.ntduc.englishconversation.data.model.Comment;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;


public interface CommentDataSource {
    Observable<Comment> createNewComment(Comment comment);

    Observable<List<Comment>> getComment(Comment lastComment);

    Observable<HashMap<Integer, Comment>> registerModifyTimelines(Comment lastComment);

    Observable<Comment> updateComment(Comment comment);

    void removeListener();

    Observable<Comment> saveRevisionComment(Comment comment);

    Observable<Comment> deleteComment(Comment comment);

    Observable<Comment> saveCommentDelete(Comment comment);

}

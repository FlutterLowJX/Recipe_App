package com.example.recipeapp.db.firebasestorage

import android.net.Uri
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object FirebaseStorageManager {
    //change this to your domain
    private const val FIREBASE_STORAGE_DOMAIN = "gs://recipe-app-89eee.appspot.com"
    const val MEMBER_IMAGE_FOLDER = "Image/"
    suspend fun uploadImage(uri: Uri, folderName: String, fileName: String): String {
        return suspendCoroutine { continuation ->
            val storageRef = Firebase.storage(FIREBASE_STORAGE_DOMAIN).reference
            val storageReference = storageRef.child("$folderName/$fileName")
            val uploadTask = storageReference.putFile(uri)
            uploadTask.addOnFailureListener {
                continuation.resumeWithException(it)
            }.addOnSuccessListener {
                it.task.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { exception ->
                            continuation.resumeWithException(exception)
                        }
                    }
                    return@Continuation storageReference.downloadUrl
                }).addOnCompleteListener { uriTask ->
                    if (uriTask.isSuccessful) {
                        val downloadUri = uriTask.result
                        continuation.resume(downloadUri.toString())
                    }
                }
            }
        }
    }

    suspend fun deleteImage(fileName: String): Void {
        return suspendCoroutine { continuation ->
            val storageRef: StorageReference = FirebaseStorage.getInstance(FIREBASE_STORAGE_DOMAIN).reference
            val storageReference = storageRef.child(fileName)
            storageReference.delete().addOnSuccessListener {
                continuation.resume(it)
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }
    }
}
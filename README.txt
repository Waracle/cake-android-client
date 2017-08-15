# Code Comments
- What libraries I would use instead of trying to re-invent the wheel_
  * Retrofit2   - For network calls, responses and serialization/deserialization with Gson.
  * RxJava      - For thread handling, action chaining and integration with Retrofit for responses.
  * okhttp3     - For the actual network calls.
  * Glide       - For image loading (or Fresco if possibility of Memory issues)
  * Auto-Value  - For models generation.
  * Icepick     - For serialization/deserialization during configuration changes.
  * ButterKnife - For simplicity.
  * Nucleus     - For MVP separation.
  * Hawk        - For local persistency
  
  
- Known bug:
  * Image loading is currently wrong on first load. (Believed issue with the way ListFrag works with ArrayAdapter.)
  * StreamUtils does unsafe placing input bytes in arraylist and not buffer. Lots of bad stuff there.
  * parseCharset can be improved.
  * AsyncTasks not thoroughly tested.

- Overall possible improvements:
  * Use RecyclerView instead of ListView!
  * Use MVP to split logic from UI.
  * Use stable serializers/deserializers for reading and writing JSON-to-Object and Object-to-JSON.
  * Use some code-generation to cut down on boilerplate.
  * No Unit tests.
  * No UI tests.

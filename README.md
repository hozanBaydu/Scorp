# Scorp
Kullanıcıların birbirine meydan okuyabildiği sosyal medya uygulaması.



https://user-images.githubusercontent.com/113553307/207135851-30b48186-847d-41bd-a991-f7ff70c037cd.mp4



## Özellikler


- Kullanıcı bir e-posta ve kullanıcı adıyla giriş yapabilir.
- İstenilen bir alanda başka bir kullanıcıya meydan okunabilir.
- Diğer meydan okumaları görüp oy kullanabilir.

Not:Yazının okunma süresi 3 dakikadır.


### Giriş ekranı

![scorpp1](https://user-images.githubusercontent.com/113553307/207135670-7ee78735-ae30-41e7-a6b2-0d88cddeb5d6.png)


Bu ekranda kullanıcı yeni hesap açabilir veya hesabı mevcutsa giriş yapabilir.

```sh
if (currentUsers!=null){
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
        }
```
Yukarıdaki kod parçası sayesinde mevcut bir kullanıcı varsa ve çıkış yapmamışsa giriş ekranını görmeden doğrudan uygulamanın içine yönlendirilir.

```sh
val profileUpdates = userProfileChangeRequest {
                            displayName = "@"+name
                        }
                        user!!.updateProfile(profileUpdates)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {                                  findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())}}
```

Yukarıdaki kodlarla yeni açılan hesap için bir kullanıcı adı oluşturulması sağlanır.
### Ana Ekranları

![scorpp2](https://user-images.githubusercontent.com/113553307/207135728-e1f66ed3-c2c7-4a8e-826e-24a763722fdf.png)

Bu ekranda kullanıcı önceden paylaşılmış meydan okumaları görüp oy kullanabilir.

```sh
var name=currentUsers?.displayName
        if (name != null) {
            viewModel.currentUserName=name
        }
```

Yukarıdaki kodlar ile mevcut kullanıcının kullanıcı ismi alınıp meydan okunan kişi ise bu meydan okumaya cevap verme yetkisi tanınır.
.
```sh
holder.binding.tagImageview.setOnClickListener {
                if (currentUsers?.displayName==holder.binding.tagName.text.toString()) {
                    holder.binding.tagImageview.findNavController().navigate(
                        MainFragmentDirections.actionMainFragmentToUploadTagFragment(
                            postList.get(position).documentId
                        ))
                }else{
                    var tageVoteNumber=holder.binding.tagVote.text.toString().toInt()+1

                    holder.binding.tagVote.text=tageVoteNumber.toString()
                }}
```

Yukarıda adaptere yazdığım kodla bu yetkinin nasıl verildiğini görebilirsiniz.

```sh
@HiltViewModel
class ScorpViewmodel @Inject constructor(
    private val firebase:Firebase,
    private val adapter: MainAdapter
):ViewModel() {
    fun updateFireebase(context: Context, selectedPicture: Uri, tagText: EditText, currentName:String,commentText: EditText){
        firebase.upload(context,selectedPicture,tagText,currentName,commentText)
    }
    var currentUserName=String()
    var postArrayList=firebase.postArrayList
    fun update(id:String,selectedPicture: Uri){
        firebase.updateData(id,selectedPicture)
    }
    fun uploadVote(id: String,vote:String){
        firebase.uploadVote(id,vote)
    }
    fun getData(context: Context){
        firebase.getdata(context)
    }
}
```

Yukarıda veri işlemleri ve aktivite arasında bağlantı olması için yazılmış viewmodeli görebilirsiniz.Bu fonksiyonların hepsi service>Firebase dosyasında yer almaktadır.Fonksiyonlar uzun ve karmaşık olduğundan burda açıklayamamaktayım.Proje dosyasından ulaşabillirsiniz.

### Veri tabanına bilgilerin gönderilmesi ekranı

![scorpp3](https://user-images.githubusercontent.com/113553307/207135758-717dc883-f34c-4507-b5a6-e4900016521d.png)


Bu ekranda gerekli izinler alınıp seçilen fotoğraf ve bilgilerin veri tabanına gönderilme işlemleri yazdığım service>Firebase dosyası sayesinde yapılır.
```sh
  fun updateData(id: String,selectedPicture: Uri) {
        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"
        val refererence = storage.reference
        val imageReference = refererence.child("images").child(imageName)
        if (selectedPicture != null) {
            imageReference.putFile(selectedPicture).addOnSuccessListener {
                val uploadPictureReference = storage.reference.child("images").child(imageName)
                uploadPictureReference.downloadUrl.addOnSuccessListener {
                  val dowlandUrl = it.toString()
                  firestore.collection("posts/").document(id).update("tagUrl", dowlandUrl)
                }
            }
        }
    }
```

Kullanıcı kendisine meydan okunan gönderiye fotoğraf eklemek için fotoğraf seçtiğinde yukarıdaki kod çalışır.Recyclerviewde tıklanan gönderinin konumu "id" anahtar kelimesiyle alındıktan sonra,kullanıcıdan alınan fotoğraf dökümana eklenir.

Okuduğunuz için teşekkür ederim.

Uygulamayı yazan:Hozan BAYDU

Tasarım:Adobexd,Canva

Tarih:20.11.2022

iletişim:hozan.baydu3447@gmail.com

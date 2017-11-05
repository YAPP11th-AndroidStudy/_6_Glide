# 6. Glide
  
__이미지 로딩 라이브러리에서 큰 선호를 받고 있는 외부 라이브러리__  
__Picasso__와 더불어 인지도가 높은 이미지 로딩 라이브러리

## 1. 서론 및 소개
![Image](https://github.com/bumptech/glide/raw/master/static/glide_logo.png)  
  
2014년 이후 공개된 구글의 라이브러리  
Bump앱을 구글이 인수하면서 bump앱에서 사용하던 이미지 라이브러리를 공개한 것

이미지를 URL로 불러올 시 고려해야할 문제들이 많다.  
1. OOM(Out Of Memory)  
2. 에러 처리 (로딩 실패 시, 재시도할 경우 등)  
  
   그외 고려할 문제와 위의 내용을 자세하게 설명한 것은 아래의 내용과 같습니다. [출처 : 박상권의 삽질블로그]  
   > 요약하면, 이미지 로딩을 구현할 때는 HTTP 통신을 안정되게 구현하고,  
   > 비트맵으로 디코딩하면서 메모리가 넘치거나 새지 않도록 주의해야 한다.  
   > 네트워크 호출과 디코딩은 단순히 백그라운드 스레드에서 동작하는 것만으로는 충분하지 않고 더 적극적으로 병렬성을 활용해야 한다.  
   > 화면 회전, 전환, 스크롤 때 반복적인 요청이 가지 않도록 이미지를 캐시하고,  
   > 불필요해진 요청은 빠른 시점에 취소해서 더 나은 UI 반응을 제공하면서 자원을 절약해야 한다.  
   > 이 과제들을 모두 해결하려다 보면 처리 흐름은 복잡해지고, 비슷한 코드가 반복되기 쉽다.  

이런 상황에서 도움을 주는 라이브러리가 Glide 입니다~ :)  
Glide 설명 문서 : [https://github.com/bumptech/glide](https://github.com/bumptech/glide) 

## 2. 가이드라인 

#### 1. Gradle 설정
<pre><code>// Project build.gradle
repositories {
  mavenCentral()
  maven { url 'https://maven.google.com' }
}

// app build.gradle
dependencies {
  compile 'com.github.bumptech.glide:glide:4.3.0'
  annotationProcessor 'com.github.bumptech.glide:compiler:4.3.0'
}</code></pre>

#### 2. GlideModule 설정
Glide 4.x 버전에서 이 과정을 진행해주어야 추가적인 기능을 사용할 수 있다!  

1. MyAppGlideModule 클래스를 생성한다.
<pre><code>// MyAppGlideModule.java
@GlideModule
public final class MyAppGlideModule extends AppGlideModule {
}
</code></pre>

2. 프로젝트를 Rebuild 한다.

#### 3. 활용

##### 1. 일반
Glide는 기본적으로 이미지를 비동기로 로드하여 이미지뷰에 보여준다.  
기본적으로 활용할 메서드는 아래와 같다.  
- with (Context context) : 안드로이드의 많은 API를 이용하기 위해 필요
- load(String imageUrl) : 웹 이미지 경로 URL or 안드로이드 리소스 ID or 로컬 파일 or URI
- into(ImageView targetImageView) : 다운로드 받은 이미지를 보여줄 이미지 뷰

<pre><code>// 웹 URL일 경우
ImageView iv = (ImageView) findViewById(R.id.iv);
String url = "https://github.com/bumptech/glide/blob/master/static/glide_logo.png";

GlideApp.with(getApplicationContext())	
        .load(url)	
        .into(iv);	


// 리소스 ID
int resId = R.mipmap.ic_launcher;

GlideApp.with(getApplicationContext())
        .load(resId)
        .into(iv);


// 로컬 파일
File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), FILE_NAME);

GlideApp.with(getApplicationContext())
        .load(file)
        .into(iv);</code></pre>

##### 2. Target을 활용
Target 활용 시 Drawable 또는 Bitmap 자체를 얻어오는 등, 여러 동작을 수행할 수 있다  
SimpleTarget을 이용한다. (BaseTarget 추상클래스를 상속받는 경우도 있다고 함)

<pre><code>// 1. 로드된 이미지를 받을 Target 생성
private SimpleTarget target = new SimpleTarget<Drawable>() {
	@Override
	public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
		//TODO:: 리소스 로드가 끝난 후 수행할 작업
		iv1.setBackground(resource);   
	}
};

2. Glide를 활용한다.
GlideApp.with(getApplicationContext())
        .load("https://github.com/bumptech/glide/blob/master/static/glide_logo.png")
        .into(target);
</code></pre>


##### 3. PlaceHolder, Error (only GlideApp)
네트워크 로드 등, 이미지 로드에 시간이 오래 걸릴 경우 빈화면 대신 PlaceHolder 이미지를 보여줄 수 있다.  
이미지 로드에 실패했을 때 등, 예상치 못한 상황으로 원본이미지 로드가 불가능 시 error내의 이미지를 보여줄 수 있다.

<pre><code>// 1. placeholder를 통해 로딩 중에 대한 대처가 가능하다.
GlideApp.with(context)
        .load("https://github.com/bumptech/glide/blob/master/static/glide_logo.png")
        .placeholder(R.mipmap.ic_launcher)
        .error(R.mipmap.ic_launcher_round)
        .into(target);
</code></pre>

##### 4. Animation(only Glide) 및 ScalsType (only GlideApp) 처리
Animation 처리도 가능하다.
<pre><code>// 1. Glide를 활용하는 경우 Animation 처리가 가능하다.
Glide.with(context)
	  .load("https://github.com/bumptech/glide/blob/master/static/glide_logo.png")
     .dontAnimate()
     .into(target);
</code></pre>

ScalsType 과정이 가능하다. (centerCrop, fitCenter)
<pre><code>// 1. Glide를 활용하는 경우 Animation 처리가 가능하다.
GlideApp.with(context)
     .load("https://github.com/bumptech/glide/blob/master/static/glide_logo.png")
     .centerCrop()    // or .fitCenter()
     .into(target);
</code></pre>

참고
1. 박상권의 삽질 블로그 : http://gun0912.tistory.com/17
2. 장범석님의 개발일지 : http://dktfrmaster.blogspot.kr/2016/09/glide.html

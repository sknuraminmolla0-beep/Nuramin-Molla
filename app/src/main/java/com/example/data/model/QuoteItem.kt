package com.example.data.model

data class QuoteItem(
  val id: Int,
  val quoteHindi: String,
  val quoteEnglish: String,
  val author: String,
  val category: String,
)

object QuoteDataSource {
  val quotes = listOf(
    QuoteItem(
      id = 1,
      quoteHindi = "सफलता की शुरुआत हमेशा एक छोटे कदम से होती है।",
      quoteEnglish = "The journey of a thousand miles begins with a single step.",
      author = "Lao Tzu",
      category = "Prerna",
    ),
    QuoteItem(
      id = 2,
      quoteHindi = "उठो, जागो और तब तक मत रुको जब तक लक्ष्य प्राप्त न हो जाए।",
      quoteEnglish = "Arise, awake, and stop not until the goal is achieved.",
      author = "Swami Vivekananda",
      category = "Lakshya",
    ),
    QuoteItem(
      id = 3,
      quoteHindi = "मुश्किलें केवल उन्हें मिलती हैं जो सही रास्ते पर चलने का हौसला रखते हैं।",
      quoteEnglish = "Difficulties come to those who have the courage to walk on the right path.",
      author = "APJ Abdul Kalam",
      category = "Housla",
    ),
    QuoteItem(
      id = 4,
      quoteHindi = "समय और धैर्य दो सबसे शक्तिशाली योद्धा हैं।",
      quoteEnglish = "Time and patience are the two most powerful warriors.",
      author = "Leo Tolstoy",
      category = "Dhairya",
    ),
    QuoteItem(
      id = 5,
      quoteHindi = "सपने वो नहीं जो हम सोते हुए देखते हैं, सपने वो हैं जो हमें सोने नहीं देते।",
      quoteEnglish = "Dreams are not what you see in sleep, dreams are what don't let you sleep.",
      author = "APJ Abdul Kalam",
      category = "Sapne",
    ),
    QuoteItem(
      id = 6,
      quoteHindi = "कल की तैयारी का सबसे अच्छा तरीका है आज पूरी लगन से काम करना।",
      quoteEnglish = "The best preparation for tomorrow is doing your best today.",
      author = "H. Jackson Brown Jr.",
      category = "Karm",
    ),
    QuoteItem(
      id = 7,
      quoteHindi = "शांति बाहर नहीं, आपके भीतर बसी होती है।",
      quoteEnglish = "Peace comes from within. Do not seek it without.",
      author = "Gautam Buddha",
      category = "Shaanti",
    ),
    QuoteItem(
      id = 8,
      quoteHindi = "अगर आप सकारात्मक सोचेंगे, तो आपके चारों ओर सकारात्मक ऊर्जा होगी।",
      quoteEnglish = "Keep your face always toward the sunshine, and shadows will fall behind you.",
      author = "Walt Whitman",
      category = "Utsaah",
    ),
  )

  fun getRandomQuote(): QuoteItem = quotes.random()
}

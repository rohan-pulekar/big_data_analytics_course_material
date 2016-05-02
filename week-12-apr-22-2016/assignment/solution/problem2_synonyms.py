# import the inaugural corpus since we will do analysis on that corpus
from nltk.corpus import inaugural

# import the wordnet corpus since we will use that to find synonyms and hyponyms
from nltk.corpus import wordnet

# create Text object out of inaugural corpus words
all_inaugural_words_text = nltk.Text(inaugural.words())

# create a frequency distribution of inaugural corpus words that are longer than 7 chars
fdist = FreqDist([word.lower() for word in all_inaugural_words_text if len(word) > 7])

# loop through the 10 most common words
for fdist_of_ten_common_words in fdist.most_common(10):

	# get the word 
	word = fdist_of_ten_common_words[0]

	# create a synonyms list
	synonyms_list = list()

	# loop through the synonyms sets of this word
	for synset in wordnet.synsets(word):

		# add lemma names of that word to the synonyms list
		synonyms_list = synonyms_list + synset.lemma_names()

	# convert the strings in synonyms list from unicode to utf-8 and to lower case
	synonyms_list = [str(synonym).lower() for synonym in synonyms_list]

	# get the unique synonyms and sort them
	synonyms_set = sorted(set(synonyms_list))

  # check if the word itself is present in the synonyms_set
  # this can happen as lemma_names() output might include the word itself
	if (word in synonyms_set):
    # if word is present in its synonyms set, remove it
		synonyms_set.remove(word)

	# print synonyms for the word
	print("\nSynonyms of '{}': {}".format(word, synonyms_set))
	print("Number of synonyms for '{}': {}".format(word, len(synonyms_set)))

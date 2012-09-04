package uk.co.fues.submission.classifier.nlp;

import java.util.ArrayList;
import java.util.List;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;

public class PhraseExtractor {

	OpenNLPWrapper opennlp;
	
	public PhraseExtractor() {
		opennlp = new OpenNLPWrapper();
	}
	
	public List<String> extractPhrases(String text) {
		List<Parse> parses = getParseTree(getSentances(text));
		List<String> candidateTopics = new ArrayList<String>();
		for (Parse parse : parses) {
			recurseParses(parse, candidateTopics);
		}
		return candidateTopics;
	}
	
	
	public List<Parse> getParseTree(String[] sentances) {
		List<Parse> parses = new ArrayList<Parse>();
		for (String sentance : sentances) {
			parses.add(ParserTool.parseLine(sentance, /*(Parser) repo.getResource(ResourceType.PARSER)*/ opennlp.getParser(), 1)[0]);
		}
		return parses;
	}
	
	
	public String[] getSentances(String document) {
		return opennlp.getSentenceDetector().sentDetect(document);
	}

	public void recurseParses(Parse parse, List<String> chosenOness) {
		
		if (parse.getChildCount() > 0) {
			for (Parse child : parse.getChildren()) {
					String phrase = extractNounPhrase(child);
						chosenOness.add(phrase);
				recurseParses(child, chosenOness);
			}
		} else {
				String phrase = extractNounPhrase(parse);
					chosenOness.add(phrase);
		}
	}
	

	private String extractNounPhrase(Parse child) {
		return child.getText().substring(child.getSpan().getStart(),
				child.getSpan().getEnd());
	}
	
}

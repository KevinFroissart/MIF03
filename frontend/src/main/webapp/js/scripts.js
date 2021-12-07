function traiteXML(XMLDoc, id)
{
	console.log(XMLDoc.documentElement.textContent);
	console.log(XMLDoc.getElementsByTagName("h")[0].childNodes[0].nodeValue);
	document.getElementById(id).innerHTML = XMLDoc.documentElement.t;
	document.getElementById(id).innerHTML = XMLDoc.documentElement.textContent;
}

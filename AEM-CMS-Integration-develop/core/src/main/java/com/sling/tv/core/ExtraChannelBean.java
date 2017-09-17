package com.sling.tv.core;

import java.util.List;
import java.util.Map;


public class ExtraChannelBean {
private List<Map<String, String>> extraChannelLogos;
private List<String> extraChannelNames;
public List<Map<String, String>> getExtraChannelLogos() {
return extraChannelLogos;
}
public void setExtraChannelLogos(List<Map<String, String>> extraChannelLogos) {
this.extraChannelLogos = extraChannelLogos;
}
public List<String> getExtraChannelNames() {
return extraChannelNames;
}
public void setExtraChannelNames(List<String> extraChannelNames) {
this.extraChannelNames = extraChannelNames;
}
}

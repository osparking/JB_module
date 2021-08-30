package com.jbpark.dabang.module;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressSearchResult {
	AddrSearchKey key;
	private List<RoadAddress> addresses;
}

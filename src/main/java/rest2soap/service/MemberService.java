package rest2soap.service;

import jakarta.inject.Singleton;
import rest2soap.repository.MemberRepository;

@Singleton
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void validate(String apiKey) {
        memberRepository.findByApiKey(apiKey)
                .orElseThrow(() -> new IllegalArgumentException("Invalid API Key"));
    }

}

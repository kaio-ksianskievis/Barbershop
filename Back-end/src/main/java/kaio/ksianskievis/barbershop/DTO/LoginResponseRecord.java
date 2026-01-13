package kaio.ksianskievis.barbershop.DTO;

import java.time.Instant;

public record LoginResponseRecord(String token, Instant expireAt,String issue) {
}

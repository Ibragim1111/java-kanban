package Manager;

import manager.Managers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestManger {
    @Test
    public void testManagerInitialization() {
        assertNotNull(Managers.getDefaultHistory());
    }
}
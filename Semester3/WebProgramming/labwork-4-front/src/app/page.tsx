import Image from "next/image";

export default function Home() {
    return (
        <section className="flex flex-col items-center justify-center gap-4 p-4">
            <Image
                src="https://i1.sndcdn.com/avatars-IDREzi3RhXdPHLsZ-MBxJoQ-t1080x1080.jpg"
                width={540}
                height={540}
                alt="mr penis"
            />
        </section>
    );
}
